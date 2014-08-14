package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import org.junit.Test;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;
import static org.truth0.Truth.ASSERT;


public class UriConfigElementTest {
    @Test
    public void shouldNotBeEmpty() {
        Optional<ValidationError> error = uri.validate("");
        ASSERT.that(error).hasValue(new ValidationError(uri.name(), "Uri is mandatory"));
    }

    @Test
    public void shouldNotStartWithSlash() {
        Optional<ValidationError> error = uri.validate("/a/b");
        ASSERT.that(error).hasValue(new ValidationError(uri.name(), "Relative uri should not start with a '/'"));
    }

    @Test
    public void shouldReturnUri() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.add(new TaskConfigProperty("uri", "google.com"));
        ASSERT.that(uri.from(taskConfig)).is("google.com");
    }
}