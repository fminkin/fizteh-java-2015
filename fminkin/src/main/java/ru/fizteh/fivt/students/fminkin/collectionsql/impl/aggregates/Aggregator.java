package ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates;

/**
 * Created by Федор on 17.12.2015.
 */
import java.util.List;
import java.util.function.Function;

public interface Aggregator<T, C> extends Function<T, C> {
    C apply(List<T> elements);
}
