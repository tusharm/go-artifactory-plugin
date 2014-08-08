package com.tw.go.plugins.artifactory.testutils.matchers;

import junit.framework.AssertionFailedError;
import org.hamcrest.Description;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

class DeepEqualsMatcherNoIgnores<T> extends DeepEqualsMatcher<T> {
    private AssertionFailedError error;

    DeepEqualsMatcherNoIgnores(T expected) {
        super(expected);
    }

    @Override
    protected boolean matchesSafely(T actual) {
        try {
            assertReflectionEquals(expected, actual);
            return true;
        } catch (AssertionFailedError error) {
            this.error = error;
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(error);
    }
}

