package org.utours.ace.profile.model;

import org.utours.ace.common.Quantity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PlayerAccount(
        String uid,
        String username,
        String email,
        Integer level,
        String description,
        Quantity<Ticket> ticketQuantity,
        Quantity<StarDust> starDustQuantity,
        List<ClassCoin> classCoins,
        Deck deck,
        Set<Card> cards
) {


    public static final int DEFAULT_LEVEL = 1;
    public static final String DEFAULT_NAME = "Player";
    public static final String DEFAULT_DESCRIPTION = "";
    public static final List<ClassCoin> DEFAULT_CLASS_COINS = Arrays.asList(ClassCoin.empty());


    public PlayerAccount starDustQuantity(Quantity<StarDust> quantity) {
        var newStarDust = new Quantity<StarDust>(quantity.quantity());
        return Builder.copyOf(this)
                .withStarDustQuantity(newStarDust)
                .build();
    }

    public PlayerAccount ticketQuantity(Quantity<Ticket> quantity) {
        var newTicketQuantity = new Quantity<Ticket>(quantity.quantity());
        return Builder.copyOf(this)
                .withTicketQuantity(newTicketQuantity)
                .build();
    }


    public PlayerAccount addCardInDeck(Card card) {

        if (deck.cards().contains(card)) {
            return this;
        }

        var deck = new HashSet<>(deck().cards());
        deck.add(card);

        return Builder.copyOf(this)
                .withDeck(new Deck(new ArrayList<>(deck)))
                .build();
    }

    public PlayerAccount replaceCardInCards(Card card) {

        Card oldC = null;
        Set<Card> cards = new HashSet<>(this.cards);
        for (var c : cards) {
            if (c.equals(card)) {
                oldC = c;
                break;
            }
        }

        if (oldC != null) {
            cards.remove(oldC);
        }

        cards.add(card);

        return PlayerAccount.Builder.copyOf(this)
                .withCards(cards)
                .build();
    }

    public PlayerAccount removeCardInDeck(Card card) {

        if (!deck.cards().contains(card)) {
            return this;
        }

        var deck = new HashSet<>(deck().cards());
        deck.remove(card);

        return Builder.copyOf(this)
                .withDeck(new Deck(new ArrayList<>(deck)))
                .build();
    }

    public static final class Builder {
        private final String uid;
        private String username = DEFAULT_NAME;
        private final String email;
        private Integer level = DEFAULT_LEVEL;
        private String description = DEFAULT_DESCRIPTION;
        private Quantity<Ticket> ticketQuantity = new Quantity<>(Ticket.DEFAULT_VALUE);
        private Quantity<StarDust> starDustQuantity = new Quantity<>(StarDust.DEFAULT_VALUE);
        private Deck deck = new Deck(new ArrayList<>());
        private Set<Card> cards = new HashSet<>();
        private List<ClassCoin> classCoins = DEFAULT_CLASS_COINS;

        private Builder(String uid, String email) {
            this.uid = uid;
            this.email = email;
        }

        public static Builder copyOf(String uid, String email) {
            return new Builder(uid, email);
        }

        public static Builder copyOf(PlayerAccount account) {
            return  Builder.copyOf(account.uid, account.email)
                    .withUsername(account.username)
                    .withLevel(account.level)
                    .withCards(account.cards)
                    .withDeck(account.deck)
                    .withTicketQuantity(account.ticketQuantity)
                    .withDescription(account.description)
                    .withStarDustQuantity(account.starDustQuantity);
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withTicketQuantity(Quantity<Ticket> ticketQuantity) {
            this.ticketQuantity = ticketQuantity;
            return this;
        }

        public Builder withLevel(Integer level) {
            this.level = level;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withCards(Set<Card> cards) {
            this.cards = cards;
            return this;
        }

        public Builder withClassCoins(List<ClassCoin> classCoins) {
            this.classCoins = classCoins;
            return this;
        }

        public Builder withStarDustQuantity(Quantity<StarDust> starDustQuantity) {
            this.starDustQuantity = starDustQuantity;
            return this;
        }

        public Builder withDeck(Deck deck) {
            this.deck = deck;
            return this;
        }

        public PlayerAccount build() {
            return new PlayerAccount(
                    uid,
                    username,
                    email,
                    level,
                    description,
                    ticketQuantity,
                    starDustQuantity,
                    classCoins,
                    deck,
                    cards
            );
        }

    }

}
