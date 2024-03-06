package org.utours.ace.common;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ProbabilityUtils {

    public static <T> T pickElement(Set<T> set, Function<T, Double> probabilitiesFunction) {
        var random = Math.random();
        var threshold = 1;

        for (T el : set) {
            var probability = threshold - probabilitiesFunction.apply(el);
            if (random >= probability) {
                return el;
            }
        }

        throw new IllegalStateException("Invalid probabilities or sum of probabilities is less than 1");
    }

}
