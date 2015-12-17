package ru.fizteh.fivt.students.fminkin.collectionsql.impl;

/**
 * Created by Федор on 17.12.2015.
 */
import javafx.util.Pair;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Aggregator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Select<T, R> {

    public boolean isDistinct() {
        return distinct;
    }

    public Function[] getFunctions() {
        return functions;
    }

    public Class getReturnClass() {
        return returnClass;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public boolean isUnion() {
        return union;
    }

    public List<T> getElements() {
        return elements;
    }

    private boolean distinct;
    private Class returnClass;
    private Function[] functions;
    private List<T> elements;
    private List<R> previousElements;
    private Predicate<T> whereCondition;
    private Comparator<R>[] comparators;
    private Predicate<R> hasCondition;
    private int numberOfObjects;
    private Function<T, ?>[] groupByConditions;
    private CQLComparator<R> cqlComparator;
    private boolean union;
    private boolean join;

    @SafeVarargs //Idea advices placing it
    public Select(List<T> newElements, Class<R> newReturnClass, boolean newDistinct, Function<T, ?>... newFunctions) {
        elements = newElements.stream().collect(Collectors.toList());
        returnClass = newReturnClass;
        distinct = newDistinct;
        functions = newFunctions;
        numberOfObjects = -1;
        union = false;
        join = false;
    }

    public Select(List<T> newElements, boolean newDistinct, Function<T, ?> first, Function<T, ?> second) {
        elements = newElements.stream().collect(Collectors.toList());
        returnClass = newElements.get(0).getClass();
        distinct = newDistinct;
        functions = new Function[]{first, second};
        numberOfObjects = -1;
        union = false;
        join = true;
    }
    @SafeVarargs //Idea advices placing it
    public Select(List<R> newpreviousElements, List<T> newElements, Class<R> newReturnClass,
                      boolean newDistinct, Function<T, ?>... newFunctions) {
        elements = newElements.stream().collect(Collectors.toList());
        returnClass = newReturnClass;
        distinct = newDistinct;
        functions = newFunctions;
        numberOfObjects = -1;
        union = true;
        previousElements = newpreviousElements;
    }

    public Select(List<R> newpreviousElements, List<T> newElements, boolean newDistinct, Function<T, ?> first,
                      Function<T, ?> second) {
        elements = newElements.stream().collect(Collectors.toList());
        returnClass = newElements.get(0).getClass();
        distinct = newDistinct;
        functions = new Function[]{first, second};
        numberOfObjects = -1;
        union = true;
        join = true;
        previousElements = newpreviousElements;
    }

    public Select<T, R> where(Predicate<T> predicate) {
        whereCondition = predicate;
        return this;
    }
    @SafeVarargs //Idea advices placing it
    public final Select<T, R> groupBy(Function<T, ?>... expressions) {
        this.groupByConditions = expressions;
        return this;
    }
    @SafeVarargs //Idea advices placing it
    public final Select<T, R> orderBy(Comparator<R>... comp) {
        comparators = comp;
        cqlComparator = new CQLComparator<R>(comp);
        return this;
    }

    public Select<T, R> hasCondition(Predicate<R> condition) {
        hasCondition = condition;
        return this;
    }

    public Select<T, R> limit(int amount) {
        numberOfObjects = amount;
        return this;
    }
    public Iterable<R> execute() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<R> result = new ArrayList<>();
        Object[] arguments = new Object[functions.length];
        Class[] returnClasses = new Class[functions.length];
        if (whereCondition != null) {
            List<T> filtered = new ArrayList<>();
            elements.stream().filter(whereCondition::test).forEach(filtered::add);
            elements = filtered;
        }
        if (groupByConditions != null) {
            Map<Integer, Integer> unique = new HashMap<>();
            String[] results = new String[groupByConditions.length];
            List<Pair<T, Integer>> output = new ArrayList<>();
            elements.stream().forEach(
                    element -> {
                        for (int i = 0; i < groupByConditions.length; i++) {
                            results[i] = (String) groupByConditions[i].apply(element);
                        }
                        if (!unique.containsKey(Objects.hash(results))) {
                            unique.put(Objects.hash(results), unique.size());
                        }
                        output.add(new Pair(element, unique.get(Objects.hash(results))));
                    }
            );
            List<List<T>> groupedData = new ArrayList<>();
            for (int i = 0; i < unique.size(); i++) {
                groupedData.add(new ArrayList<T>());
            }
            for (Pair<T, Integer> element : output) {
                groupedData.get(element.getValue()).add(element.getKey());
            }
            for (List<T> group : groupedData) {
                for (int i = 0; i < functions.length; i++) {
                    if (functions[i] instanceof  Aggregator) {
                        arguments[i] = ((Aggregator) functions[i]).apply(group);
                    } else {
                        arguments[i] = functions[i].apply(group.get(0));
                    }
                    returnClasses[i] = arguments[i].getClass();
                }
                if (join) {
                    Tuple newElement = new Tuple(arguments[0], arguments[1]);
                    result.add((R) newElement);
                } else {
                    R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                    result.add(newElement);
                }
            }
        } else {
            for (T element : this.elements) {
                for (int i = 0; i < functions.length; i++) {
                    arguments[i] = functions[i].apply(element);
                    if (functions[i] instanceof  Aggregator) {
                        List<T> currArg = new ArrayList<>();
                        currArg.add(element);
                        arguments[i] = ((Aggregator) functions[i]).apply(currArg);
                    } else {
                        arguments[i] = functions[i].apply(element);
                    }
                    returnClasses[i] = arguments[i].getClass();
                }
                if (join) {
                    Tuple newElement = new Tuple(arguments[0], arguments[1]);
                    result.add((R) newElement);
                } else {
                    R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                    result.add(newElement);
                }
            }
        }
        if (hasCondition != null) {
            List<R> filtered = new ArrayList<>();
            result.stream().filter(hasCondition::test).forEach(filtered::add);
            result = filtered;
        }
        if (distinct) {
            Set<Integer> hash = new HashSet<>();
            List<Integer> flags = new ArrayList<>();
            for (R element : result) {
                if (!hash.contains(element.toString().hashCode())) {
                    flags.add(1);
                    hash.add(element.toString().hashCode());
                } else {
                    flags.add(0);
                }
            }
            List<R> distinctElements = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                if (flags.get(i) == 1) {
                    distinctElements.add(result.get(i));
                }
            }
            result = distinctElements;
        }
        if (comparators != null) {
            result.sort(cqlComparator);
        }
        if (numberOfObjects != -1) {
            while (result.size() > numberOfObjects) {
                result.remove(result.size() - 1);
            }
        }
        if (union) {
            previousElements.addAll(result);
            result = previousElements;
        }
        return result;
    }

    public Union<T, R> union() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        List<R> result = (List<R>) this.execute();
        if (join) {
            return new Union<>(result, true);
        } else {
            return new Union<>(result);
        }
    }

    public class CQLComparator<K> implements Comparator<K> {
        private Comparator<K>[] comparators;
        @SafeVarargs //Idea advices placing it
        public CQLComparator(Comparator<K>... comparators) {
            this.comparators = comparators;
        }

        @Override
        public int compare(K first, K second) {
            for (Comparator<K> comparator : comparators) {
                if (comparator.compare(first, second) != 0) {
                    return comparator.compare(first, second);
                }
            }
            return 0;
        }
    }

}
