package ru.fizteh.fivt.students.fminkin.collectionsql.impl;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.fminkin.collectionsql.Aggregates;
import ru.fizteh.fivt.students.fminkin .collectionsql.CollectionsQL;
import ru.fizteh.fivt.students.fminkin.collectionsql.OrderByConditions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by Федор on 17.12.2015.
 */


@RunWith(MockitoJUnitRunner.class)
public class SelectTest extends TestCase {

    private List<CollectionsQL.Student> exampleList, emptyExampleList, distinctExampleList;
    private Function<CollectionsQL.Student, Double> functionAge;
    private Function<CollectionsQL.Student, String> functionName, functionGroup;
    private CollectionsQL.Student student;
    private Select<CollectionsQL.Student, CollectionsQL.Student> select, distinctSelect;
    private Select<CollectionsQL.Student, CollectionsQL.Statistics> groupSelect;

    @Before
    public void setUp() throws Exception {
        exampleList = new ArrayList<>();
        emptyExampleList = new ArrayList<>();
        distinctExampleList = new ArrayList<>();
        exampleList.add(new CollectionsQL.Student("garkaviy", LocalDate.parse("1997-02-20"), "495"));
        exampleList.add(new CollectionsQL.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        exampleList.add(new CollectionsQL.Student("kargaltsev", LocalDate.parse("1997-02-20"), "495"));
        exampleList.add(new CollectionsQL.Student("zertsalov", LocalDate.parse("1996-10-29"), "495"));
        distinctExampleList.add(new CollectionsQL.Student("garkaviy", LocalDate.parse("1997-02-20"), "495"));
        distinctExampleList.add(new CollectionsQL.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        distinctExampleList.add(new CollectionsQL.Student("garkaviy", LocalDate.parse("1997-02-20"), "495"));
        distinctExampleList.add(new CollectionsQL.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        functionAge = CollectionsQL.Student::age;
        functionName = CollectionsQL.Student::getName;
        functionGroup = CollectionsQL.Student::getGroup;
        student = new CollectionsQL.Student("zertsalov", LocalDate.parse("1996-10-29"), "495");
        select = From.from(exampleList).select(CollectionsQL.Student.class, CollectionsQL.Student::getName,
                CollectionsQL.Student::getGroup);
        distinctSelect = From.from(distinctExampleList).selectDistinct(CollectionsQL.Student.class,
                CollectionsQL.Student::getName, CollectionsQL.Student::getGroup);
        groupSelect = From.from(exampleList).select(CollectionsQL.Statistics.class,
                CollectionsQL.Student::getGroup, Aggregates.count(CollectionsQL.Student::getName));
    }

    @Test
    public void testWhere() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) select
                .where(s -> Objects.equals(s.getGroup(), "494"))
                .execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testGroupBy() throws Exception {
        List<CollectionsQL.Statistics> result = (List<CollectionsQL.Statistics>) groupSelect
                .groupBy(CollectionsQL.Student::getGroup)
                .execute();
        List<CollectionsQL.Statistics> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Statistics("495", 3));
        resultList.add(new CollectionsQL.Statistics("494", 1));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testOrderBy() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) select
                .orderBy(OrderByConditions.desc(CollectionsQL.Student::getGroup))
                .execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("kargaltsev", "495"));
        resultList.add(new CollectionsQL.Student("zertsalov", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testHaving() throws Exception {
        List<CollectionsQL.Statistics> result = (List<CollectionsQL.Statistics>) groupSelect
                .groupBy(CollectionsQL.Student::getGroup)
                .hasCondition(s -> Objects.equals(s.getGroup(), "494"))
                .execute();
        List<CollectionsQL.Statistics> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Statistics("494", 1));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testLimit() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) select
                .limit(2)
                .execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }

        result = (List<CollectionsQL.Student>) select
                .limit(5)
                .execute();
        resultList.clear();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        resultList.add(new CollectionsQL.Student("kargaltsev", "495"));
        resultList.add(new CollectionsQL.Student("zertsalov", "495"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testExecute() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) select.execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        resultList.add(new CollectionsQL.Student("kargaltsev", "495"));
        resultList.add(new CollectionsQL.Student("zertsalov", "495"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }

        List<CollectionsQL.Statistics> resultWithAggr = (List<CollectionsQL.Statistics>) groupSelect.execute();
        List<CollectionsQL.Statistics> resultListWithAggr = new ArrayList<>();
        resultListWithAggr.add(new CollectionsQL.Statistics("495", 1));
        resultListWithAggr.add(new CollectionsQL.Statistics("494", 1));
        resultListWithAggr.add(new CollectionsQL.Statistics("495", 1));
        resultListWithAggr.add(new CollectionsQL.Statistics("495", 1));
        assertEquals(resultWithAggr.size(), resultListWithAggr.size());
        for (int i = 0; i < resultWithAggr.size(); i++) {
            assertEquals(resultListWithAggr.get(i).toString(), resultWithAggr.get(i).toString());
        }
    }

    @Test
    public void testUnion() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) select.union().
                from(exampleList).
                select(CollectionsQL.Student.class, CollectionsQL.Student::getName,
                        CollectionsQL.Student::getGroup).
                execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        resultList.add(new CollectionsQL.Student("kargaltsev", "495"));
        resultList.add(new CollectionsQL.Student("zertsalov", "495"));
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        resultList.add(new CollectionsQL.Student("kargaltsev", "495"));
        resultList.add(new CollectionsQL.Student("zertsalov", "495"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testIsDistinct() throws Exception {
        List<CollectionsQL.Student> result = (List<CollectionsQL.Student>) distinctSelect.execute();
        List<CollectionsQL.Student> resultList = new ArrayList<>();
        resultList.add(new CollectionsQL.Student("garkaviy", "495"));
        resultList.add(new CollectionsQL.Student("iglina", "494"));
        assertEquals(resultList.size(), result.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).toString(), result.get(i).toString());
        }
    }
}
