package org.utours.ace.infra.game.firebase;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;

import java.util.stream.Collectors;

public class FirebaseGamePartyMapper {

    private final FirebasePlayerMapper playerMapper;
    private final FirebaseCardMapper cardMapper;

    public FirebaseGamePartyMapper(FirebasePlayerMapper playerMapper) {
        this.cardMapper = new FirebaseCardMapper();
        this.playerMapper = playerMapper;
    }

    GameParty map(FirebaseGameParty fgp) {
        return GameParty.Builder.of()
                .withPlayer(playerMapper.map(fgp.getFirstPlayer()))
                .withOpponent(playerMapper.map(fgp.getSecondPlayer()))
                .withOpponentBoard(mapBoard(fgp.getSecondPlayerBoard()))
                .withPlayerBoard(mapBoard(fgp.getFirstPlayerBoard()))
                .withNumberTurn(fgp.getNumberTurn())
                .withWaiting(fgp.getWaiting())
                .build();
    }

    Board mapBoard(FirebaseBoard board) {
        return new Board(board.getBoard().stream().map(firebaseSlot -> {
            if (firebaseSlot.getCard() == null) {
                return new Slot.SEmpty();
            }
            return new Slot.SCard(cardMapper.map(firebaseSlot.getCard()));
        }).collect(Collectors.toList()));
    }

}
