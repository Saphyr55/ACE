package org.utours.ace.profile.model;

import java.util.Set;

public record Deck(
        Set<Card> cards
) {

    public static final int MAX_SLOT = 20;

    public Deck {
        if (cards.size() > MAX_SLOT) {
            throw new IllegalStateException();
        }
    }

}
