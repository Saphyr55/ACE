package org.utours.ace.infra.game.firebase;

import org.utours.ace.game.model.Player;
import org.utours.ace.infra.profile.firebase.FirebaseCard;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;
import org.utours.ace.profile.model.ClassCoin;

import java.util.List;

public class FirebasePlayerMapper {

    private final FirebaseCardMapper cardMapper;

    public FirebasePlayerMapper() {
        this.cardMapper = new FirebaseCardMapper();
    }

    public Player map(FirebasePlayer firebasePlayer) {
        return Player.Builder.of()
                .withPlayerID(firebasePlayer.getPlayerID())
                .withActions(List.of())
                .withHand(cardMapper.mapFs(firebasePlayer.getHand()))
                .build();
    }

    public FirebasePlayer map(Player player) {
        FirebasePlayer firebasePlayer = new FirebasePlayer();
        firebasePlayer.setPlayerID(player.playerID());
        firebasePlayer.setHand(cardMapper.map(player.hand()));
        return firebasePlayer;
    }


}
