package org.utours.ace.profile.model;

import java.util.List;

public record Deck(
        List<Card> cards
) {

    public Deck {
        if (cards.size() > 20) {
            throw new IllegalStateException();
        }
    }

}
