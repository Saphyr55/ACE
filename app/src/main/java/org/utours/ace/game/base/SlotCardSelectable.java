package org.utours.ace.game.base;

import android.animation.ObjectAnimator;
import android.view.View;

import org.utours.ace.common.Selectable;
import org.utours.ace.game.model.GameParty;

import java.util.List;
import java.util.function.Supplier;

public class SlotCardSelectable implements Selectable<Integer> {

    private final CardSelectable<Integer> wrapped;
    private final Supplier<GameParty> gamePartySupplier;
    private final List<View> playerBoard;

    public SlotCardSelectable(Supplier<GameParty> gamePartySupplier, List<View> playerBoard) {
        this.gamePartySupplier = gamePartySupplier;
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
