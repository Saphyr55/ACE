package org.utours.ace.infra.game.firebase;

import org.utours.ace.game.model.Player;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;
import org.utours.ace.profile.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FirebasePlayerMapper {

    private final FirebaseCardMapper cardMapper;

    public FirebasePlayerMapper() {
        this.cardMapper = new FirebaseCardMapper();
    }

    public Player map(FirebasePlayer firebasePlayer) {
        return Player.Builder.of()
                .withPlayerID(firebasePlayer.getPlayerID())
                .withActions(new ArrayList<>())
                .withHand(cardMapper.mapFs(firebasePlayer.getHand()))
                .withDeck(new Deck(Set.copyOf(cardMapper.mapFs(firebasePlayer.getDeck()))))
                .build();
    }

    public FirebasePlayer map(Player player) {
        FirebasePlayer firebasePlayer = new FirebasePlayer();
        firebasePlayer.setPlayerID(player.playerID());
        firebasePlayer.setHand(cardMapper.map(player.hand()));
        firebasePlayer.setDeck(cardMapper.map(List.copyOf(player.deck().cards())));
        return firebasePlayer;
    }


}
