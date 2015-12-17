package ru.fizteh.fivt.students.fminkin.collectionsql.impl;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.fminkin.collectionsql.CollectionsQL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
/**
 * Created by Федор on 17.12.2015.
 */

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class FromTest extends TestCase {
    private List<CollectionsQL.Student> exampleList, emptyExampleList;
    private Function<CollectionsQL.Student, Double> functionAge;
    private Function<CollectionsQL.Student, String> functionName, functionGroup;
    private CollectionsQL.Student student;

    @Before
    public void setUp() throws Exception {
        exampleList = new ArrayList<>();
        emptyExampleList = new ArrayList<>();
        exampleList.add(new CollectionsQL.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        exampleList.add(new CollectionsQL.Student("kargaltsev", LocalDate.parse("1997-02-20"), "495"));
        exampleList.add(new CollectionsQL.Student("zertsalov", LocalDate.parse("1996-10-29"), "495"));
        functionAge = CollectionsQL.Student::age;
        functionName = CollectionsQL.Student::getName;
        functionGroup = CollectionsQL.Student::getGroup;
        student = new CollectionsQL.Student("zertsalov", LocalDate.parse("1996-10-29"), "495");
    }

    @Test
    public void testFrom() throws Exception {
        From<CollectionsQL.Student> fromStmt = From.from(exampleList);
        assertEquals(fromStmt.getElements().size(), exampleList.size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(fromStmt.getElements().get(i), exampleList.get(i));
        }
    }

    @Test
    public void testSelect() throws Exception {
        Select<CollectionsQL.Student, CollectionsQL.Student> select = From.from(exampleList)
                .select(CollectionsQL.Student.class, CollectionsQL.Student::getName,
                        CollectionsQL.Student::getGroup);
        assertEquals(select.getNumberOfObjects(), -1);
        assertEquals(select.getReturnClass(), CollectionsQL.Student.class);
        assertEquals(select.isDistinct(), false);
        assertEquals(select.isUnion(), false);
        Function<CollectionsQL.Student, String>[] functions = new Function[2];
        functions[0] = CollectionsQL.Student::getName;
        functions[1] = CollectionsQL.Student::getGroup;
        assertEquals(select.getFunctions().length, functions.length);
        for (int i = 0; i < functions.length; i++) {
            for (CollectionsQL.Student element : exampleList) {
                assertEquals(functions[i].apply(element),
                        select.getFunctions()[i].apply(element));
            }
        }
        assertEquals(exampleList.size(), select.getElements().size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(exampleList.get(i), select.getElements().get(i));
        }
    }

    @Test
    public void testSelectDistinct() throws Exception {
        Select<CollectionsQL.Student, CollectionsQL.Student> select = From.from(exampleList)
                .selectDistinct(CollectionsQL.Student.class, CollectionsQL.Student::getName,
                        CollectionsQL.Student::getGroup);
        assertEquals(select.getNumberOfObjects(), -1);
        assertEquals(select.getReturnClass(), CollectionsQL.Student.class);
        assertEquals(select.isDistinct(), true);
        assertEquals(select.isUnion(), false);
        Function<CollectionsQL.Student, String>[] functions = new Function[2];
        functions[0] = CollectionsQL.Student::getName;
        functions[1] = CollectionsQL.Student::getGroup;
        assertEquals(select.getFunctions().length, functions.length);
        for (int i = 0; i < functions.length; i++) {
            for (CollectionsQL.Student element : exampleList) {
                assertEquals(functions[i].apply(element),
                        select.getFunctions()[i].apply(element));
            }
        }
        assertEquals(exampleList.size(), select.getElements().size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(exampleList.get(i), select.getElements().get(i));
        }
    }
}
