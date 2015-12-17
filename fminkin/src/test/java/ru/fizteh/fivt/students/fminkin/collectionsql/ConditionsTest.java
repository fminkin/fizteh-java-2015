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
import static ru.fizteh.fivt.students.fminkin.collectionsql.Conditions.rlike;
/**
 * Created by Федор on 17.12.2015.
 */



@RunWith(MockitoJUnitRunner.class)
public class ConditionsTest extends TestCase {
    private Function<CollectionsQL.Student, String> function = CollectionsQL.Student::getName;

    @Test
    public void testRlike() throws Exception {
        List<CollectionsQL.Student> correct = new ArrayList<>();
        correct.add(student("minkin", LocalDate.parse("2001-03-11"), "494"));
        correct.add(student("ivanov", LocalDate.parse("2000-02-12"), "495"));
        correct.add(student("stepanov", LocalDate.parse("2002-01-13"), "495"));

        assertEquals(rlike(function, ".*in").test(correct.get(0)), true);
        assertEquals(rlike(function, ".*ov").test(correct.get(1)), true);
        assertEquals(rlike(function, ".*ov").test(correct.get(2)), true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike() throws Exception {
        Conditions.like(function, "aaa");
    }
}
