package org.utours.ace.game.model;

import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;

import java.util.ArrayList;
import java.util.List;

public record Player(
        String playerID,
        List<Card> hand,
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

        public Builder withHand(List<Card> hand) {
            this.hand = hand;
            return this;
        }

        public Builder withPlayerID(String playerID) {
            this.playerID = playerID;
            return this;
        }

        public Player build() {
            return new Player(playerID, hand, actions);
        }

    }

}
