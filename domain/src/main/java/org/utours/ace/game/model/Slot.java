package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

public sealed interface Slot {

    record SCard(Card card) implements Slot { }

    record SEmpty() implements Slot { }

}
