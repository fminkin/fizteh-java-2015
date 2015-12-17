package ru.fizteh.fivt.students.fminkin.collectionsql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Федор on 16.12.2015.
 */

public class Sources {
    public static <Type> List<Type> list(Type... data) {
        return new ArrayList<>(Arrays.asList(data));
    }

}
