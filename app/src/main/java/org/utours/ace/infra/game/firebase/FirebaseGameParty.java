package org.utours.ace.infra.game.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;

@Bean
@FirebaseModel
public class FirebaseGameParty {

    private String uid;
    private FirebasePlayer firstPlayer;
    private FirebasePlayer secondPlayer;
    private FirebasePlayer currentPlayer;
    private FirebaseBoard firstPlayerBoard = new FirebaseBoard();
    private FirebaseBoard secondPlayerBoard = new FirebaseBoard();
    private int numberTurn = 0;
    private boolean waiting = true;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public FirebasePlayer getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(FirebasePlayer firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public FirebasePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(FirebasePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public FirebasePlayer getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(FirebasePlayer secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public FirebaseBoard getFirstPlayerBoard() {
        return firstPlayerBoard;
    }

    public void setFirstPlayerBoard(FirebaseBoard firstPlayerBoard) {
        this.firstPlayerBoard = firstPlayerBoard;
    }

    public FirebaseBoard getSecondPlayerBoard() {
        return secondPlayerBoard;
    }

    public void setSecondPlayerBoard(FirebaseBoard secondPlayerBoard) {
        this.secondPlayerBoard = secondPlayerBoard;
    }

    public int getNumberTurn() {
        return numberTurn;
    }

    public void setNumberTurn(int numberTurn) {
        this.numberTurn = numberTurn;
    }

    public boolean getWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

}
