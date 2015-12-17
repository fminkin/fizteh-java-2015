package ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates;

/**
 * Created by Федор on 17.12.2015.
 */
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Count<T> implements Aggregator<T, Integer> {

    private Function<T, ?> function;
    public Count(Function<T, ?> expression) {
        this.function = expression;
    }

    @Override
    public Integer apply(List<T> elements) {
        Set<Object> distinctElements = new HashSet<>();
        for (T element : elements) {
            if (!distinctElements.contains(function.apply(element))) {
                distinctElements.add(function.apply(element));
            }
        }
        return distinctElements.size();
    }
    @Override
    public Integer apply(T t) {
        return null;
    }
}
