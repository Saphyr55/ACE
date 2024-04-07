package org.utours.ace.common;

import java.util.function.Consumer;

public class SelectableValue<T> implements Selectable<T> {

    private T value;
    private final Consumer<T> onSelect;
    private final Consumer<T> onUnselect;

    public SelectableValue() {
        value = null;
        this.onSelect = t -> { };
        this.onUnselect = onSelect;
    }

    public SelectableValue(Consumer<T> onSelect, Consumer<T> onUnselect) {
        value = null;
        this.onSelect = onSelect;
        this.onUnselect = onUnselect;
    }

    @Override
    public void select(T value) {
        bind(value);
    }

    @Override
    public void unselect() {
        if (isSelect())
            bind(value);
    }

    @Override
    public void bind(T value) {
        if (this.value == value || this.value != null) {
            onUnselect.accept(this.value);
        }
        if (this.value != value) {
            onSelect.accept(value);
            this.value = value;
        } else {
            this.value = null;
        }
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean isSelect() {
        return value != null;
    }


}
