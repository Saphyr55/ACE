package org.utours.ace.common;

import android.view.View;

import java.util.function.Consumer;

public interface Selectable<T> {

    void select(T value);

    void unselect();

    void bind(T value);

    T getValue();

    boolean isSelect();

}
