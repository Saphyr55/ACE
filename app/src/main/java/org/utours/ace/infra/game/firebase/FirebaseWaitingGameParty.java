package org.utours.ace.infra.game.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;

@Bean
@FirebaseModel
public class FirebaseWaitingGameParty {
    
    private String firstPlayerID;

    public String getFirstPlayerID() {
        return firstPlayerID;
    }

    public void setFirstPlayerID(String firstPlayerID) {
        this.firstPlayerID = firstPlayerID;
    }

}
