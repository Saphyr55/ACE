package org.utours.ace.game.use_case;

import android.view.View;

import org.utours.ace.common.Selectable;

import java.util.function.Consumer;

public class CardViewSelectable implements Selectable<View> {

    private View selectedCardView;
    private final Consumer<View> onSelect;
    private final Consumer<View> onUnselect;

    public CardViewSelectable(Consumer<View> onSelect, Consumer<View> onUnselect) {
        selectedCardView = null;
        this.onSelect = onSelect;
        this.onUnselect = onUnselect;
    }

    @Override
    public void select(View view) {
        bind(view);
    }

    @Override
    public void unselect() {
        bind(selectedCardView);
    }

    @Override
    public void bind(View view) {
        if (selectedCardView == view || selectedCardView != null) {
            onUnselect.accept(selectedCardView);
        }
        if (selectedCardView != view) {
            onSelect.accept(view);
            selectedCardView = view;
        } else {
            selectedCardView = null;
        }
    }

    @Override
    public View getValue() {
        return selectedCardView;
    }


}
