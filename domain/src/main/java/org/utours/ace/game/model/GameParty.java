package org.utours.ace.game.model;

import org.utours.ace.profile.model.Card;

import java.util.ArrayList;
import java.util.List;

public record GameParty(
        Player player,
        Player opponent,
        Player currentPlayer,
        Board playerBoard,
        Board opponentBoard,
        int numberTurn
) {

    public GameParty incrementNumberTurn() {
        return GameParty.Builder.copyOf(this)
                .withNumberTurn(numberTurn + 1)
                .build();
    }

    public GameParty nextTurn() {

        var gameParty = incrementNumberTurn();
        Player newCurrentPlayer = gameParty.isPlayerTurn()
                ? gameParty.opponent()
                : gameParty.player();

        return GameParty.Builder.copyOf(gameParty)
                .withCurrentPlayer(newCurrentPlayer)
                .build();
    }

    public boolean isPlayerTurn() {
        return isCurrentPlayer(player());
    }

    public boolean isOpponentTurn() {
        return isCurrentPlayer(opponent());
    }

    private boolean isCurrentPlayer(Player player) {
        return player.playerID().equals(currentPlayer().playerID());
    }

    public static class Builder {

        private Player player;
        private Player opponent;
        private Player currentPlayer;
        private Board playerBoard = new Board();
        private Board opponentBoard = new Board();
        private int numberTurn = 0;

        public static Builder newInstance() {
            return new Builder();
        }

        public static Builder copyOf(GameParty gameParty) {
            return newInstance()
                    .withPlayerBoard(gameParty.playerBoard)
                    .withOpponentBoard(gameParty.opponentBoard)
                    .withCurrentPlayer(gameParty.currentPlayer)
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

        public Builder withCurrentPlayer(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
            return this;
        }

        public GameParty build() {
            return new GameParty(
                    player,
                    opponent,
                    currentPlayer,
                    playerBoard,
                    opponentBoard,
                    numberTurn
            );
        }

    }

}
