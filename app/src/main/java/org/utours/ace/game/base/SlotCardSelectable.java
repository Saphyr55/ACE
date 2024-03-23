package org.utours.ace.game.base;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.utours.ace.R;
import org.utours.ace.common.Selectable;

import java.util.List;

public class SlotCardSelectable implements Selectable<Integer> {

    private final CardSelectable<Integer> wrapped;
    private final List<View> playerBoard;

    public SlotCardSelectable(List<View> playerBoard) {
        this.playerBoard = playerBoard;
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
        animate(playerBoard.get(i), 20);
    }

    private void stopAnimation(Integer i) {
        animate(playerBoard.get(i), 0);
    }

    private void animate(View view, float value) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", value);
        animation.setDuration(200);
        animation.start();
    }

}
