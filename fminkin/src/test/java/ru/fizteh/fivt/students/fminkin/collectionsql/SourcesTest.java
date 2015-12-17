package ru.fizteh.fivt.students.fminkin.collectionsql;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static ru.fizteh.fivt.students.fminkin.collectionsql.CollectionsQL.Student.student;

@RunWith(MockitoJUnitRunner.class)
public class SourcesTest extends TestCase {

    @Test
    public void testList() throws Exception {
        List<CollectionsQL.Student> correct = new ArrayList<>();
        correct.add(student("minkin", LocalDate.parse("2012-12-17"), "494"));
        correct.add(student("ivanov", LocalDate.parse("2011-12-17"), "495"));
        correct.add(student("stepanov", LocalDate.parse("2010-12-17"), "495"));

        List<CollectionsQL.Student> result = Sources.list(
                student("minkin", LocalDate.parse("2012-12-17"), "494"),
                student("ivanov", LocalDate.parse("2011-12-17"), "495"),
                student("stepanov", LocalDate.parse("2010-12-17"), "495"));
        assertEquals(correct.size(), result.size());
        for (int i = 0; i < correct.size(); i++) {
            assertEquals(correct.get(i).toString(), result.get(i).toString());
        }
    }
}
