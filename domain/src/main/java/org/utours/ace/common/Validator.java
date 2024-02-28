package org.utours.ace.common;

import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validator<T> {

    private final T t;
    private final Stack<Throwable> throwableStack = new Stack<>();
    
    public static <T>  Validator<T> of(T type) {
        return new Validator<>(type);
    }

    private Validator(T t) {
        this.t = t;
    }

    public Validator<T> validate(Predicate<T> predicate) {
        return validate(predicate, "");
    }

    public Validator<T> invalidate(Predicate<T> predicate) {
        return invalidate(predicate, "");
    }

    public Validator<T> validate(Predicate<T> predicate, String message) {
        if (!predicate.test(t)) throwableStack.add(new IllegalStateException(message));
        return this;
    }

    public Validator<T> invalidate(Predicate<T> predicate, String message) {
        if (predicate.test(t)) throwableStack.add(new IllegalStateException(message));
        return this;
    }

    public <R> Validator<R> map(Function<T, R> map) {
        return Validator.of(map.apply(t));
    }

    public boolean isValid() {
        return throwableStack.isEmpty();
    }

    public Validator<T> ifValid(Consumer<T> consumer) {
        if (isValid()) consumer.accept(t);
        return this;
    }

    public boolean isInvalid() {
        return !isValid();
    }

    public T get() {
        if (isValid()) return t;
        IllegalStateException e = new IllegalStateException();
        throwableStack.forEach(e::addSuppressed);
        throw e;
    }

    public Validator<T> trace(BiConsumer<T, Stack<Throwable>> biConsumer) {
        biConsumer.accept(t, throwableStack);
        return this;
    }

}
