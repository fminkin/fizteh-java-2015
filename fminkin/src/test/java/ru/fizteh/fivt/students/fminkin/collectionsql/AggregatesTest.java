package ru.fizteh.fivt.students.fminkin.collectionsql;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.fminkin.collectionsql.impl.aggregates.Aggregator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Федор on 17.12.2015.
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class AggregatesTest extends TestCase {

    private List<CollectionsQL.Student> correct, emptylist;
    private Function<CollectionsQL.Student, Double> functionAge;
    private Function<CollectionsQL.Student, String> functionName, functionGroup;
    private CollectionsQL.Student student;

    @Before
    public void setUp() throws Exception {
        correct = new ArrayList<>();
        emptylist = new ArrayList<>();
        correct.add(new CollectionsQL.Student("minkin", LocalDate.parse("1997-03-30"), "494"));
        correct.add(new CollectionsQL.Student("ivanov", LocalDate.parse("1997-01-01"), "495"));
        correct.add(new CollectionsQL.Student("stepanov", LocalDate.parse("1997-02-02"), "495"));
        functionAge = CollectionsQL.Student::age;
        functionName = CollectionsQL.Student::getName;
        functionGroup = CollectionsQL.Student::getGroup;
        student = new CollectionsQL.Student("batman", LocalDate.parse("1996-10-29"), "495");
    }

    @Test
    public void testMax() throws Exception {
        assertEquals(((Aggregator) Aggregates.max(functionGroup)).apply(correct), "495");
        assertEquals(((Aggregator) Aggregates.max(functionName)).apply(correct), "stepanov");
        assertEquals(((Aggregator) Aggregates.max(functionAge)).apply(correct), 18.0);

        assertEquals(((Aggregator) Aggregates.max(functionAge)).apply(student), null);
        assertEquals(((Aggregator) Aggregates.max(functionAge)).apply(emptylist), null);
    }

    @Test
    public void testMin() throws Exception {
        assertEquals(((Aggregator) Aggregates.min(functionGroup)).apply(correct), "494");
        assertEquals(((Aggregator) Aggregates.min(functionName)).apply(correct), "ivanov");
        assertEquals(((Aggregator) Aggregates.min(functionAge)).apply(correct), 18.0);

        assertEquals(((Aggregator) Aggregates.min(functionAge)).apply(student), null);
        assertEquals(((Aggregator) Aggregates.min(functionAge)).apply(emptylist), null);
    }

    @Test
    public void testCount() throws Exception {
        assertEquals(((Aggregator) Aggregates.count(functionGroup)).apply(correct), 2);
        assertEquals(((Aggregator) Aggregates.count(functionName)).apply(correct), 3);
        assertEquals(((Aggregator) Aggregates.count(functionAge)).apply(correct), 1);

        assertEquals(((Aggregator) Aggregates.count(functionAge)).apply(student), null);
    }

    @Test
    public void testAvg() throws Exception {
        assertEquals((Double) ((Aggregator) Aggregates.avg(functionAge)).apply(correct), 18.0, 0.1);

        assertEquals(((Aggregator) Aggregates.avg(functionAge)).apply(student), null);
    }
}

