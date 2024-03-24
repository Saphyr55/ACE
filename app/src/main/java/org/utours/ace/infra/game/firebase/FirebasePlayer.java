package org.utours.ace.infra.game.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;
import org.utours.ace.infra.profile.firebase.FirebaseCard;

import java.util.List;

@Bean
@FirebaseModel
public class FirebasePlayer {

    private String playerID;

    private List<FirebaseCard> hand = List.of();


    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public List<FirebaseCard> getHand() {
        return hand;
    }

    public void setHand(List<FirebaseCard> hand) {
        this.hand = hand;
    }
}
