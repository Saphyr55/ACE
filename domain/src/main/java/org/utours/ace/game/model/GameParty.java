package org.utours.ace.game.model;

import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.PlayerAction;

public record GameParty(
        String uid,
        String currentPlayerUID,
        Player firstPlayer,
        Player secondPlayer,
        Board firstPlayerBoard,
        Board secondPlayerBoard,
        int numberTurn,
        boolean isWaiting
) {

    public static int MAX_SLOT = 8;

    public GameParty addPlayerAction(PlayerAction playerAction, GamePartyMapper gamePartyMapper) {
        var gameParty = this;
        gameParty.currentPlayer().actions().add(playerAction);
        var actions = gameParty.currentPlayer().actions();
        if (actions.size() % 3 == 0 && !actions.isEmpty()) {
            gameParty = gamePartyMapper.map(gameParty);
            gameParty = gameParty.nextTurn();
            gameParty = gamePartyMapper.map(gameParty);
        }
        return GameParty.Builder.copyOf(gameParty).build();
    }

    public GameParty nextTurn() {
        var actions = currentPlayer().actions();
        if (actions.size() % 3 != 0) {
            for (int i = 0; i < actions.size() % 3; i++) {
                actions.add(PlayerAction.noAction());
            }
        }
        return GameParty.Builder.copyOf(this)
                .withNumberTurn(numberTurn + 1)
                .withCurrentPlayerUID(opponentPlayer().playerID())
                .build();
    }

    public boolean isCurrentPlayer(Player player) {
        return player.playerID().equals(currentPlayerUID);
    }

    public Player currentPlayer() {
        return isCurrentPlayer(secondPlayer)
                ? secondPlayer
                : firstPlayer;
    }

    public Board currentPlayerBoard() {
        return isCurrentPlayer(secondPlayer)
                ? secondPlayerBoard
                : firstPlayerBoard;
    }

    public Board opponentPlayerBoard() {
        return isCurrentPlayer(secondPlayer)
                ? firstPlayerBoard
                : secondPlayerBoard;
    }

    public Player opponentPlayer() {
        return isCurrentPlayer(secondPlayer)
                ? firstPlayer
                : secondPlayer;
    }

    public Player oppositePlayerOf(Player player) {
        return player.playerID().equals(firstPlayer.playerID())
                ? secondPlayer
                : firstPlayer;
    }

    public static class Builder {

        private String uid;
        private String currentPlayerUID;
        private Player firstPlayer;
        private Player secondPlayer;
        private Board firstPlayerBoard = new Board();
        private Board secondPlayerBoard = new Board();
        private int numberTurn = 0;
        private boolean isWaiting = true;

        public static Builder of() {
            return new Builder();
        }

        public static Builder copyOf(GameParty gameParty) {
            return of()
                    .withUid(gameParty.uid)
                    .withCurrentPlayerUID(gameParty.currentPlayerUID)
                    .withFirstPlayer(Player.Builder.copyOf(gameParty.firstPlayer).build())
                    .withFirstPlayerBoard(gameParty.firstPlayerBoard)
                    .withSecondPlayer(gameParty.secondPlayer)
                    .withSecondPlayerBoard(gameParty.secondPlayerBoard)
                    .withWaiting(gameParty.isWaiting)
                    .withNumberTurn(gameParty.numberTurn);
        }

        public Builder withUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder withWaiting(boolean waiting) {
            isWaiting = waiting;
            return this;
        }

        public Builder withNumberTurn(int numberTurn) {
            this.numberTurn = numberTurn;
            return this;
        }

        public Builder withFirstPlayer(Player player) {
            this.firstPlayer = player;
            return this;
        }

        public Builder withFirstPlayerBoard(Board playerBoard) {
            this.firstPlayerBoard = playerBoard;
            return this;
        }

        public Builder withSecondPlayer(Player player) {
            this.secondPlayer = player;
            return this;
        }

        public Builder withSecondPlayerBoard(Board playerBoard) {
            this.secondPlayerBoard = playerBoard;
            return this;
        }

        public Builder withCurrentPlayerUID(String currentPlayerUID) {
            this.currentPlayerUID = currentPlayerUID;
            return this;
        }

        public Builder withCurrentPlayer(Player player) {
            if (currentPlayerUID.equals(firstPlayer.playerID())) {
                firstPlayer = player;
            } else {
                secondPlayer = player;
            }
            return this;
        }

        public Builder withCurrentPlayerBoard(Board board) {
            if (currentPlayerUID.equals(firstPlayer.playerID())) {
                firstPlayerBoard = board;
            } else {
                secondPlayerBoard = board;
            }
            return this;
        }

        public Builder withOpponent(Player opponent) {
            if (!currentPlayerUID.equals(firstPlayer.playerID())) {
                firstPlayer = opponent;
            } else {
                secondPlayer = opponent;
            }
            return this;
        }

        public Builder withOpponentBoard(Board board) {
            if (!currentPlayerUID.equals(firstPlayer.playerID())) {
                firstPlayerBoard = board;
            } else {
                secondPlayerBoard = board;
            }
            return this;
        }

        public GameParty build() {

            if (currentPlayerUID == null)
                currentPlayerUID = firstPlayer.playerID();

            return new GameParty(
                    uid,
                    currentPlayerUID,
                    firstPlayer,
                    secondPlayer,
                    firstPlayerBoard,
                    secondPlayerBoard,
                    numberTurn,
                    isWaiting
            );
        }

    }

}
