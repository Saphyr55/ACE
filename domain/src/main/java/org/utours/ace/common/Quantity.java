package org.utours.ace.common;

import java.util.function.Function;

public record Quantity<T>(int quantity)
        implements Comparable<Quantity<T>> {

    public static <T> Quantity<T> zero() {
        return new Quantity<>(0);
    }

    public static <T> Quantity<T> of(int quantity) {
        return new Quantity<>(quantity);
    }

    public Quantity<T> sub(int q) {
        return new Quantity<>(quantity() - q);
    }

    public Quantity<T> add(int q) {
        return new Quantity<>(quantity() + q);
    }

    public Quantity<T> sub(Quantity<T> q) {
        return new Quantity<>(quantity() - q.quantity());
    }

    public Quantity<T> add(Quantity<T> q) {
        return new Quantity<>(quantity() + q.quantity());
    }

    public Quantity<T> apply(Function<Integer, Integer> function) {
        return new Quantity<>(function.apply(quantity()));
    }

    public <R> Quantity<R> map(Function<Quantity<T>, Quantity<R>> mapper) {
        return mapper.apply(this);
    }

    @Override
    public int compareTo(Quantity<T> o) {
        return Integer.compare(o.quantity(), quantity());
    }

}
