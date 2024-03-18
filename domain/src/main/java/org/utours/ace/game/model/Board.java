package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

import java.util.List;

public record Board(
        List<Card> cards
) {

    public Board() {
        this(List.of());
    }

}
