package com.tw.go.plugins.artifactory.testutils.matchers;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.hamcrest.Description;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static com.tw.go.plugins.artifactory.testutils.matchers.FieldHasDifferentValuesIn.fieldHasDifferentValues;
import static java.util.Arrays.asList;

class DeepEqualsMatcherWithIgnores<T> extends DeepEqualsMatcher<T> {
    private List<String> ignoredFieldNames;

    DeepEqualsMatcherWithIgnores(T expected, List<String> ignoredFieldNames) {
        super(expected);
        this.ignoredFieldNames = ignoredFieldNames;
    }

    @Override
    protected boolean matchesSafely(T actual) {
        Optional<Field> fieldWithDifferentValues = tryFind(unignoredFields(), fieldHasDifferentValues(expected, actual));
        return !fieldWithDifferentValues.isPresent();
    }

    @Override
    public void describeTo(Description description) {
        super.describeTo(description);
        description.appendText(" while ignoring fields: " + ignoredFieldNames);
    }

    private List<Field> unignoredFields() {
        List<Field> declaredFields = asList(expected.getClass().getDeclaredFields());

        return newArrayList(filter(declaredFields, new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return !ignoredFieldNames.contains(field.getName());
            }
        }));
    }
}


