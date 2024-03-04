package org.utours.ace.profile.model;

import org.utours.ace.common.Quantity;

import java.util.ArrayList;
import java.util.List;

public record PlayerAccount(
        String uid,
        String username,
        String email,
        Integer level,
        String description,
        Quantity<StarDust> starDustQuantity,
        Deck deck,
        List<Card> cards
) {

    public PlayerAccount starDustQuantity(Quantity<StarDust> quantity) {
        var newStarDust = new Quantity<StarDust>(quantity.quantity());
        return new PlayerAccount(
                uid, username, email, level, description, newStarDust, deck, cards);
    }

    public PlayerAccount addCardInDeck(Card card) {

        if (deck.cards().contains(card)) {
            return this;
        }

        var deck = new ArrayList<>(deck().cards());
        deck.add(card);

        return new PlayerAccount(
                uid, username, email, level, description, starDustQuantity,
                new Deck(List.copyOf(deck)), cards
        );
    }

    public PlayerAccount removeCardInDeck(Card card) {

        if (!deck.cards().contains(card)) {
            return this;
        }

        var deck = new ArrayList<>(deck().cards());
        deck.remove(card);

        return new PlayerAccount(
                uid, username, email, level, description, starDustQuantity,
                new Deck(List.copyOf(deck)), cards
        );
    }

}
