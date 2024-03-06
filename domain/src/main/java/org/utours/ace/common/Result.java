package org.utours.ace.common;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Result<T, E> {

    private final T value;
    private final E error;

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public static  <T, E> Result<T, E> successOf(T value) {
        return new Result<>(value, null);
    }

    public static  <T, E> Result<T, E> errorOf(E error) {
        return new Result<>(null, error);
    }

    public <T1, E1> Result<T1, E1> map(
            Function<Result<T, E>, Result<T1, E1>> mapper) {

        return mapper.apply(this);
    }

    public <T1> Result<T1, E> mapValue(Function<T, T1> mapper) {
        if (isSuccess()) {
            return successOf(mapper.apply(value));
        }
        return errorOf(error);
    }

    public <E1> Result<T, E1> mapError(Function<E, E1> mapper) {
        if (isError()) {
            return errorOf(mapper.apply(error));
        }
        return successOf(value);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isError() {
        return value == null;
    }

    public E getError() {
        return error;
    }

    public T getValue() {
        return value;
    }

}
