package com.tw.go.plugins.artifactory.testutils.matchers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

class DeepEqualsMatcherNoIgnores<T> extends DeepEqualsMatcher<T> {
    DeepEqualsMatcherNoIgnores(T expected) {
        super(expected);
    }

    @Override
    protected boolean matchesSafely(T actual) {
        try {
            assertReflectionEquals(expected, actual);
            return true;
        } catch (Throwable error) {
            return false;
        }
    }
}

