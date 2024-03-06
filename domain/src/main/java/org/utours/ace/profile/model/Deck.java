package org.utours.ace.profile.model;

import org.utours.ace.common.Validator;
import org.utours.ace.profile.feature.CardRegistry;

import java.util.Set;
import java.util.function.Supplier;

public record Deck(
        Set<Card> cards // Immutable set.
) {

    public static final int MAX_SLOT = 20;

    public Deck {
        if (cards.size() > MAX_SLOT) {
            throw new IllegalArgumentException();
        }
    }

}
