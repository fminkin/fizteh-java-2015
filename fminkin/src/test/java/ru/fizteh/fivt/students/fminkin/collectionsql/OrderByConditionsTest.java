package ru.fizteh.fivt.students.fminkin.collectionsql;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static ru.fizteh.fivt.students.fminkin.collectionsql.CollectionsQL.Student.student;
/**
 * Created by Федор on 17.12.2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class OrderByConditionsTest extends TestCase {
    private Function<CollectionsQL.Student, String> function = CollectionsQL.Student::getName;

    @Test
    public void testAsc() throws Exception {
        List<CollectionsQL.Student> correct = new ArrayList<>();
        correct.add(student("ivanov", LocalDate.parse("2001-12-12"), "494"));
        correct.add(student("minkin", LocalDate.parse("2002-11-12"), "495"));
        correct.add(student("stepanov", LocalDate.parse("1999-02-12"), "495"));
        assertTrue(OrderByConditions.asc(function).compare(correct.get(0), correct.get(2)) < 0);
        assertTrue(OrderByConditions.asc(function).compare(correct.get(1), correct.get(0)) > 0);
        assertTrue(OrderByConditions.asc(function).compare(correct.get(0), correct.get(0)) == 0);
    }

    @Test
    public void testDesc() throws Exception {
        List<CollectionsQL.Student> correct = new ArrayList<>();
        correct.add(student("minkin", LocalDate.parse("2001-12-12"), "494"));
        correct.add(student("ivanov", LocalDate.parse("2002-11-12"), "495"));
        correct.add(student("stepanov", LocalDate.parse("1999-02-12"), "495"));
        assertTrue(OrderByConditions.desc(function).compare(correct.get(0), correct.get(2)) > 0);
        assertTrue(OrderByConditions.desc(function).compare(correct.get(1), correct.get(0)) > 0);
        assertTrue(OrderByConditions.desc(function).compare(correct.get(0), correct.get(0)) == 0);
    }
}
