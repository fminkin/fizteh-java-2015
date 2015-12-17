package ru.fizteh.fivt.students.fminkin.collectionsql;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Average;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Count;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Max;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Min;

import java.util.function.Function;
/**
 * Created by Федор on 17.12.2015.
 */

public class Aggregates {
    public static <T, R extends Comparable> Function<T, R> max(Function<T, R> expression) {
        return new Max<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new Min<>(expression);
    }

    public static <T> Function<T, Integer> count(Function<T, ?> expression) {
        return new Count<>(expression);
    }

    public static <T> Function<T, Double> avg(Function<T, ? extends Number> expression) {
        return new Average<>(expression);
    }

}
