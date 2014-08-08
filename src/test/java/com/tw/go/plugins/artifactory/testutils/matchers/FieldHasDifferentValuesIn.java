package com.tw.go.plugins.artifactory.testutils.matchers;

import com.google.common.base.Predicate;

import java.lang.reflect.Field;

import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static org.unitils.reflectionassert.ReflectionComparatorMode.LENIENT_DATES;

class FieldHasDifferentValuesIn implements Predicate<Field> {
    private Object actual;
    private Object expected;

    public static FieldHasDifferentValuesIn fieldHasDifferentValues(Object expected, Object actual) {
        return new FieldHasDifferentValuesIn(expected, actual);
    }

    private FieldHasDifferentValuesIn(Object expected, Object actual) {
        this.actual = actual;
        this.expected = expected;
    }

    @Override
    public boolean apply(Field field) {
        return !fieldHasValue(actual, field.getName(), fieldValue(expected, field));
    }

    private boolean fieldHasValue(Object object, String fieldName, Object fieldValue) {
        try {
            assertPropertyReflectionEquals(fieldName, fieldValue, object, LENIENT_DATES);
            return true;
        } catch (Throwable error) {
            return false;
        }
    }

    private Object fieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException("Unable to access field: " + field.getName());
        }
    }

}
