package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

import java.util.List;

public record Player(
        String playerID,
        List<Card> hand
) {

    public static final int HAND_CAPACITY = 5;

    public Player {
        if (hand.size() > HAND_CAPACITY) {
            throw new IllegalArgumentException();
        }
    }

}
