package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

import java.util.List;

public record Player(
        List<Card> hand
) {

    public Player {
        if (hand.size() > 5) {
            throw new IllegalArgumentException();
        }
    }

}
