package org.utours.ace.infra.game.firebase;

import com.google.firebase.database.DatabaseReference;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.GamePartyFinder;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FirebaseGamePartyFinder implements GamePartyFinder {

    private final DatabaseReference databaseReference;
    private final FirebasePlayerMapper playerMapper;
    private final FirebaseGamePartyMapper gamePartyMapper;

    public FirebaseGamePartyFinder(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.playerMapper = new FirebasePlayerMapper();
        this.gamePartyMapper = new FirebaseGamePartyMapper(playerMapper);
    }

    @Override
    public CompletableFuture<Optional<GameParty>> find(Player player) {

        CompletableFuture<Optional<GameParty>> completableFuture = new CompletableFuture<>();

        databaseReference
                .orderByChild(FirebaseGameParty.WAITING_REF)
                .equalTo(true)
                .limitToFirst(1)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    FirebaseGameParty fgp = dataSnapshot.getValue(FirebaseGameParty.class);
                    assert fgp != null;
                    completableFuture.complete(Optional.of(gamePartyMapper.map(fgp)));
                }).addOnFailureListener(e -> {
                    completableFuture.complete(Optional.empty());
                });

        return completableFuture;
    }

    @Override
    public CompletableFuture<GameParty> join(Player player) {
        return find(player).thenApply(gp -> gp.orElseGet(() -> {
            FirebaseGameParty fgp = new FirebaseGameParty();
            fgp.setUid(UUID.randomUUID().toString());
            fgp.setFirstPlayer(playerMapper.map(player));
            databaseReference.child(fgp.getUid()).setValue(fgp);
            return gamePartyMapper.map(fgp);
        }));
    }

}
