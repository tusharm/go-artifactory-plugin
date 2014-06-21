package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import org.junit.Test;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;
import static org.truth0.Truth.ASSERT;


public class UriConfigElementTest {
    @Test
    public void shouldNotBeEmpty() {
        Optional<ValidationError> error = uri.validate("");
        ASSERT.that(error).hasValue(new ValidationError(uri.name(), "Invalid uri"));
    }

    @Test
    public void shouldNotStartWithSlash() {
        Optional<ValidationError> error = uri.validate("/a/b");
        ASSERT.that(error).hasValue(new ValidationError(uri.name(), "Invalid uri"));
    }
}