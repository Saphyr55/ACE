package org.utours.ace.game.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Board(
        List<Slot> cards
) {

    public static int MAX_SLOT = 8;

    public Board() {
        this(IntStream.range(0, MAX_SLOT)
                .mapToObj(operand -> new Slot.SEmpty())
                .collect(Collectors.toList()));
    }

}
