package org.utours.ace.infra.game.firebase;

import org.utours.ace.common.annotation.Bean;
import org.utours.ace.infra.common.firebase.annotation.FirebaseModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Bean
@FirebaseModel
public class FirebaseBoard {

    private List<FirebaseSlot> board = IntStream.range(0, 8)
            .mapToObj(operand -> new FirebaseSlot())
            .collect(Collectors.toList());

    public List<FirebaseSlot> getBoard() {
        return board;
    }

    public void setBoard(List<FirebaseSlot> board) {
        this.board = board;
    }

}
