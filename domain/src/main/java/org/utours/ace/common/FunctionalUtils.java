package org.utours.ace.common;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FunctionalUtils {

    public static <T, R> CompletableFuture<List<R>> listOfFuturesToFutureList(List<CompletableFuture<T>> listOfFutures,
                                                                              Function<T, R> mapper) {

        Function<List<CompletableFuture<T>>, List<R>> transformFunction =
                futures -> futures.stream()
                        .map(rCompletableFuture -> mapper.apply(rCompletableFuture.join()))
                        .collect(Collectors.toList());

        return CompletableFuture.allOf(listOfFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> transformFunction.apply(listOfFutures));
    }

}
