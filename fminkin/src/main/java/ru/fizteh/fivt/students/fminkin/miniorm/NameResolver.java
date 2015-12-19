package ru.fizteh.fivt.students.fminkin.miniorm;
import com.google.common.base.CaseFormat;

/**
 * Created by Федор on 19.12.2015.
 */



class NameResolver {
    static final String REGEX = "[A-Za-z0-9_-]*";
    public static Boolean isGood(String name) {
        return name.matches(REGEX);
    }
    static String convertCamelToUnderscore(String name) {
        if (Character.isLowerCase(name.charAt(0))) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }
}
