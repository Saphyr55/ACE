package org.utours.ace.game.base;

import android.animation.ObjectAnimator;
import android.view.View;

import org.utours.ace.common.Selectable;

import java.util.List;
import java.util.function.Function;

public class SlotCardSelectable implements Selectable<Integer> {

    private final CardSelectable<Integer> wrapped;
    private final List<View> playerBoard;
    private final List<View> opponentBoard;
    private boolean isOpponent;

    public SlotCardSelectable(List<View> playerBoard,
                              List<View> opponentBoard,
                              boolean isOpponent) {

        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.isOpponent = isOpponent;
        this.wrapped = new CardSelectable<>(this::startAnimation, this::stopAnimation);
    }

    @Override
    public void select(Integer value) {
        wrapped.select(value);
    }

    @Override
    public void unselect() {
        wrapped.unselect();
    }

    @Override
    public void bind(Integer value) {
        wrapped.bind(value);
    }

    @Override
    public Integer getValue() {
        return wrapped.getValue();
    }

    @Override
    public boolean isSelect() {
        return wrapped.isSelect();
    }

    private void startAnimation(Integer i) {
        var board = playerBoard;
        if (isOpponent) {
            board = opponentBoard;
        }
        animate(board.get(i), 20);
    }

    private void stopAnimation(Integer i) {
        var board = playerBoard;
        if (isOpponent) {
            board = opponentBoard;
        }
        animate(board.get(i), 0);
    }

    private void animate(View view, float value) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", value);
        animation.setDuration(200);
        animation.start();
    }

    public void setOpponent(boolean opponent) {
        isOpponent = opponent;
    }

}
