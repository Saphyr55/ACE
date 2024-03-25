package org.utours.ace.infra.game.firebase;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirebaseGamePartyMapper {

    private final FirebasePlayerMapper playerMapper;
    private final FirebaseCardMapper cardMapper;

    public FirebaseGamePartyMapper(FirebasePlayerMapper playerMapper) {
        this.cardMapper = new FirebaseCardMapper();
        this.playerMapper = playerMapper;
    }

    GameParty map(FirebaseGameParty fgp) {
        return GameParty.Builder.of()
                .withUid(fgp.getUid())
                .withCurrentPlayerUID(fgp.getCurrentPlayerUID())
                .withFirstPlayer(playerMapper.map(fgp.getFirstPlayer()))
                .withFirstPlayerBoard(mapBoard(fgp.getFirstPlayerBoard()))
                .withOpponent(playerMapper.map(fgp.getSecondPlayer()))
                .withOpponentBoard(mapBoard(fgp.getSecondPlayerBoard()))
                .withNumberTurn(fgp.getNumberTurn())
                .withWaiting(fgp.getWaiting())
                .build();
    }

    FirebaseGameParty map(GameParty gp) {
        FirebaseGameParty fgp = new FirebaseGameParty();
        fgp.setUid(gp.uid());
        fgp.setNumberTurn(gp.numberTurn());
        fgp.setSecondPlayer(playerMapper.map(gp.secondPlayer()));
        fgp.setFirstPlayer(playerMapper.map(gp.firstPlayer()));
        fgp.setCurrentPlayerUID(gp.currentPlayerUID());
        fgp.setFirstPlayerBoard(mapBoard(gp.firstPlayerBoard()));
        fgp.setSecondPlayerBoard(mapBoard(gp.secondPlayerBoard()));
        return fgp;
    }


    Board mapBoard(FirebaseBoard board) {
        return new Board(IntStream.range(0, 8)
                .mapToObj(i -> {
                    var fbSlot = board.getBoard().get(i);
                    if (fbSlot == null || fbSlot.getCard() == null)
                        return new Slot.SEmpty();
                    return new Slot.SCard(cardMapper.map(fbSlot.getCard()));
                }).collect(Collectors.toList()));
    }

    FirebaseBoard mapBoard(Board board) {
        FirebaseBoard firebaseBoard = new FirebaseBoard();
        firebaseBoard.setBoard(IntStream.range(0, 8)
                .mapToObj(i -> {
                    FirebaseSlot firebaseSlot = new FirebaseSlot();
                    var slot = board.slots().get(i);
                    if (slot instanceof Slot.SCard sCard) {
                        firebaseSlot.setEmpty(false);
                        firebaseSlot.setCard(cardMapper.map(sCard.card()));
                    }
                    return firebaseSlot;
                }).collect(Collectors.toList()));
        return firebaseBoard;
    }

}
