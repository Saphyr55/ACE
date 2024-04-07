package org.utours.ace.infra.game.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;
import org.utours.ace.infra.profile.firebase.FirebaseCard;

@Bean
@FirebaseModel
public class FirebaseSlot {

    private FirebaseCard card = null;
    private boolean empty = true;

    public FirebaseCard getCard() {
        return card;
    }

    public void setCard(FirebaseCard card) {
        this.card = card;
    }


    public boolean getEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

}
