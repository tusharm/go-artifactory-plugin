package com.tw.go.plugins.artifactory.testutils.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

import static java.util.Arrays.asList;

public abstract class DeepEqualsMatcher<T> extends TypeSafeMatcher<T> {

    public static <T> Matcher<T> deepEquals(T expected) {
        return new DeepEqualsMatcherNoIgnores<>(expected);
    }

    public static <T> Matcher<T> deepEquals(T expected, Ignored ignoredFields) {
        return new DeepEqualsMatcherWithIgnores<>(expected, ignoredFields.names());
    }

    public static Ignored ignoring(String... ignoredFields) {
        return new Ignored(ignoredFields);
    }

    protected T expected;

    protected DeepEqualsMatcher(T expected) {
        this.expected = expected;
    }

    static class Ignored {
        private List<String> ignored;

        private Ignored(String... ignoredNames) {
            this.ignored = asList(ignoredNames);
        }

        List<String> names() {
            return ignored;
        }
    }
}
