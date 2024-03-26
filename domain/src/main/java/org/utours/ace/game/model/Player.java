package org.utours.ace.game.model;

import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record Player(
        String playerID,
        List<Card> hand,
        Deck deck,
        List<PlayerAction> actions
) {

    public static final int HAND_CAPACITY = 5;

    public Player {
        if (hand.size() > HAND_CAPACITY) {
            throw new IllegalArgumentException();
        }
    }

    public static class Builder {

        private String playerID = "-1";
        private List<Card> hand = new ArrayList<>();
        private Deck deck = new Deck(Set.of());
        private List<PlayerAction> actions = new ArrayList<>();

        public static Builder of() {
            return new Builder();
        }

        public static Builder copyOf(Player player) {
            return of()
                    .withPlayerID(player.playerID)
                    .withHand(player.hand)
                    .withActions(player.actions);
        }

        public Builder withActions(List<PlayerAction> actions) {
            this.actions = actions;
            return this;
        }

        public Builder withDeck(Deck deck) {
            this.deck = deck;
            return this;
        }

        public Builder withHand(List<Card> hand) {
            this.hand = hand;
            return this;
        }

        public Builder withPlayerID(String playerID) {
            this.playerID = playerID;
            return this;
        }

        public Player build() {
            return new Player(playerID, hand, deck, actions);
        }

    }

}
