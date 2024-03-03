package org.utours.ace.profile.model;

import java.util.ArrayList;
import java.util.List;

public record PlayerAccount(
        String uid,
        String username,
        String email,
        Integer level,
        String description,
        Deck deck,
        List<Card> cards
) {

    public PlayerAccount addCardInDeck(Card card) {
        if (deck.cards().contains(card)) return this;
        var deck = new ArrayList<>(deck().cards());
        deck.add(card);
        return new PlayerAccount(
                uid, username, email, level, description,
                new Deck(List.copyOf(deck)), cards
        );
    }

    public PlayerAccount removeCardInDeck(Card card) {
        if (!deck.cards().contains(card)) return this;
        var deck = new ArrayList<>(deck().cards());
        deck.remove(card);
        return new PlayerAccount(
                uid, username, email, level, description,
                new Deck(List.copyOf(deck)), cards
        );
    }

}
