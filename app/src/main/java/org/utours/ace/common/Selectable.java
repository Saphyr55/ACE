package org.utours.ace.common;

import android.view.View;

import java.util.function.Consumer;

public interface Selectable<T> {

    void select(T view);

    void unselect();

    void bind(View view);

    T getValue();

}
