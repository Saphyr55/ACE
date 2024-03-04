package org.utours.ace.profile.model;

import java.util.List;

public record Deck(
        List<Card> cards
) {

    public static final int MAX_SLOT = 20;

    public Deck {
        if (cards.size() > MAX_SLOT) {
            throw new IllegalStateException();
        }
    }

}
