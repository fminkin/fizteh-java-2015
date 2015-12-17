package ru.fizteh.fivt.students.fminkin.collectionsql.impl;

public class Tuple<F, S> {

    private final F first;
    private final S second;

    public Tuple(F newFirst, S newSecond) {
        first = newFirst;
        second = newSecond;
    }
    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Tuple{" + "first =" + first + ", second =" + second + "}\n";
    }
}
