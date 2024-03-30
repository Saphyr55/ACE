package org.utours.ace.infra.game.firebase;

import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;
import org.utours.ace.profile.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirebasePlayerMapper {

    private final FirebaseCardMapper cardMapper;

    public FirebasePlayerMapper() {
        this.cardMapper = new FirebaseCardMapper();
    }

    public Player map(FirebasePlayer firebasePlayer) {
        return Player.Builder.of()
                .withPlayerID(firebasePlayer.getPlayerID())
                .withActions(IntStream.range(0, firebasePlayer.getNumberAction())
                        .mapToObj(value -> PlayerAction.noAction())
                        .collect(Collectors.toList()))
                .withHand(cardMapper.mapFs(firebasePlayer.getHand()))
                .withDeck(new Deck(new ArrayList<>(cardMapper.mapFs(firebasePlayer.getDeck()))))
                .build();
    }

    public FirebasePlayer map(Player player) {
        FirebasePlayer firebasePlayer = new FirebasePlayer();
        firebasePlayer.setPlayerID(player.playerID());
        firebasePlayer.setHand(cardMapper.map(player.hand()));
        firebasePlayer.setDeck(cardMapper.map(List.copyOf(player.deck().cards())));
        firebasePlayer.setNumberAction(player.actions().size());
        return firebasePlayer;
    }


}
