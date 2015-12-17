package ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates;

/**
 * Created by Федор on 17.12.2015.
 */

import java.util.List;
import java.util.function.Function;

public class Max<T, R extends Comparable<R>> implements Aggregator<T, R> {
    private Function<T, R> function;
    public Max(Function<T, R> expression) {
        this.function = expression;
    }

    @Override
    public R apply(List<T> elements) {
        if (elements.isEmpty()) {
            return null;
        }
        T result = elements.get(0);
        for (T element : elements) {
            if (function.apply(result).compareTo(function.apply(element)) < 0) {
                result = element;
            }
        }
        return function.apply(result);
    }

    @Override
    public R apply(T t) {
        return null;
    }
}
