package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

import java.util.ArrayList;
import java.util.List;

public record GameParty(
        Player player,
        Player opponent,
        Board playerBoard,
        Board opponentBoard,
        int numberTurn
) {

    public GameParty emplacePlayerCardInBoard(Card card) {

        List<Card> cards = new ArrayList<>(player.hand());
        cards.remove(card);
        Player newPlayer = new Player(List.copyOf(cards));

        cards = new ArrayList<>(playerBoard.cards());
        cards.add(card);
        Board newPlayerBoard = new Board(List.copyOf(cards));

        return GameParty.Builder.of(this)
                .withPlayer(newPlayer)
                .withPlayerBoard(newPlayerBoard)
                .build();
    }

    public static class Builder {

        private Player player = new Player(List.of());;
        private Player opponent = new Player(List.of());
        private Board playerBoard = new Board();
        private Board opponentBoard = new Board();
        private int numberTurn = 0;

        private Builder() { }

        public static Builder newInstance() {
            return new Builder();
        }

        public static Builder of(GameParty gameParty) {
            return newInstance()
                    .withPlayerBoard(gameParty.playerBoard)
                    .withOpponentBoard(gameParty.opponentBoard)
                    .withOpponent(gameParty.opponent)
                    .withPlayer(gameParty.player)
                    .withNumberTurn(gameParty.numberTurn);
        }

        public Builder withNumberTurn(int numberTurn) {
            this.numberTurn = numberTurn;
            return this;
        }

        public Builder withOpponent(Player opponent) {
            this.opponent = opponent;
            return this;
        }

        public Builder withOpponentBoard(Board opponentBoard) {
            this.opponentBoard = opponentBoard;
            return this;
        }

        public Builder withPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withPlayerBoard(Board playerBoard) {
            this.playerBoard = playerBoard;
            return this;
        }

        public GameParty build() {
            return new GameParty(
                    player,
                    opponent,
                    playerBoard,
                    opponentBoard,
                    numberTurn
            );
        }

    }

}
