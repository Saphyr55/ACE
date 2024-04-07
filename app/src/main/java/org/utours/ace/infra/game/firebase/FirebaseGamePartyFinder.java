package org.utours.ace.infra.game.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.GamePartyFinder;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class FirebaseGamePartyFinder implements GamePartyFinder {

    private final PlayerAccountRepository playerAccountRepository;
    private final DatabaseReference gamePartiesRef;
    private final DatabaseReference waitingGamePartiesRef;
    private final CardService cardService;
    private final FirebasePlayerMapper playerMapper;
    private final FirebaseGamePartyMapper gamePartyMapper;

    public FirebaseGamePartyFinder(PlayerAccountRepository playerAccountRepository,
                                   CardService cardService,
                                   DatabaseReference gamePartiesRef,
                                   DatabaseReference waitingGamePartiesRef) {

        this.playerAccountRepository = playerAccountRepository;
        this.cardService = cardService;
        this.gamePartiesRef = gamePartiesRef;
        this.waitingGamePartiesRef = waitingGamePartiesRef;
        this.playerMapper = new FirebasePlayerMapper();
        this.gamePartyMapper = new FirebaseGamePartyMapper(playerMapper);
    }

    @Override
    public CompletableFuture<GameParty> join(PlayerAccount playerAccount,
                                             Consumer<Player> playerConsumer,
                                             Consumer<GameParty> onUpdate) {

        var gamePartyFuture = new CompletableFuture<GameParty>();
        var wUidPaFuture = new CompletableFuture<Map.Entry<String, String>>();

        var query = waitingGamePartiesRef.limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (var da : snapshot.getChildren()) {
                        var partyUid = da.getKey();
                        var wPlayerUid = da.getValue(String.class);

                        assert partyUid != null;
                        assert wPlayerUid != null;

                        wUidPaFuture.complete(Map.entry(wPlayerUid, partyUid));
                        query.removeEventListener(this);
                        return;
                    }
                } else {
                    var uid = UUID.randomUUID().toString();
                    waitingGamePartiesRef.child(uid).setValue(playerAccount.uid());
                    gamePartiesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            var fgp = snapshot.child(uid).getValue(FirebaseGameParty.class);
                            if (fgp != null) {
                                playerConsumer.accept(playerMapper.map(fgp.getSecondPlayer()));
                                addOnUpdate(uid, onUpdate);
                                var gp = gamePartyMapper.map(fgp);
                                gamePartyFuture.complete(gp);
                                gamePartiesRef.removeEventListener(this);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", error.getMessage(), error.toException());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage(), error.toException());
            }

        });

        AtomicReference<String> partyUid = new AtomicReference<>();
        wUidPaFuture.thenCompose(s -> {
            var wUidPa = s.getKey();
            partyUid.set(s.getValue());
            return playerAccountRepository.getByUID(wUidPa);
        }).thenAccept(pa -> {

            var pp = mapAccount(pa.orElseThrow(RuntimeException::new));
            var fFirstPlayer = playerMapper.map(pp);
            var fSecondPlayer = playerMapper.map(mapAccount(playerAccount));

            FirebaseGameParty firebaseGameParty = new FirebaseGameParty();
            firebaseGameParty.setUid(partyUid.get());
            firebaseGameParty.setCurrentPlayerUID(fFirstPlayer.getPlayerID());
            firebaseGameParty.setFirstPlayer(fFirstPlayer);
            firebaseGameParty.setSecondPlayer(fSecondPlayer);

            playerConsumer.accept(pp);

            waitingGamePartiesRef.child(partyUid.get()).removeValue();
            gamePartiesRef.child(partyUid.get()).setValue(firebaseGameParty);

            addOnUpdate(partyUid.get(), onUpdate);

            var gp = gamePartyMapper.map(firebaseGameParty);
            gamePartyFuture.complete(gp);
        });

        return gamePartyFuture;
    }

    private Player mapAccount(PlayerAccount playerAccount) {

        assert playerAccount != null;

        Deck deck = playerAccount.deck();
        List<Card> cards = cardService.getCardsStochastic(deck.cards(),
                Player.HAND_CAPACITY, true);

        return Player.Builder.of()
                .withPlayerID(playerAccount.uid())
                .withDeck(deck)
                .withHand(cards)
                .build();
    }

    private void addOnUpdate(String uid, Consumer<GameParty> onUpdate) {
        gamePartiesRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                var fgp = snapshot.getValue(FirebaseGameParty.class);
                if (snapshot.exists() && fgp != null)
                    onUpdate.accept(gamePartyMapper.map(fgp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage(), error.toException());
            }
        });

    }

}
