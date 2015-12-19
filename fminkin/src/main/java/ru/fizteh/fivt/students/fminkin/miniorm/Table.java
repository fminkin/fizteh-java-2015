package ru.fizteh.fivt.students.fminkin.miniorm;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Федор on 19.12.2015.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name() default "";
}
