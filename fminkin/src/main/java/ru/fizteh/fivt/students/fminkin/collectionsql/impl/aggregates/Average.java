package ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates;

/**
 * Created by Федор on 17.12.2015.
 */
import java.util.List;
import java.util.function.Function;

public class Average<T> implements Aggregator<T, Double> {
    private Function<T, ? extends Number> function;
    public Average(Function<T, ? extends Number> expression) {
        this.function = expression;
    }

    @Override
    public Double apply(List<T> elements) {
        Double result = 0.0;
        for (T element : elements) {
            result += (Double) function.apply(element);
        }
        return result / elements.size();
    }

    @Override
    public Double apply(T t) {
        return null;
    }
}
