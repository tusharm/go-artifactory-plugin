package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import org.junit.Test;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uriConfig;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;


public class UriConfigElementTest {
    @Test
    public void shouldNotHaveAnEmptyUri() {
        Optional<ValidationError> error = uriConfig.validate(uriConfig(""));
        ASSERT.that(error).hasValue(new ValidationError(uriConfig.name(), "Uri is mandatory"));
    }

    @Test
    public void shouldNotHaveUriStartingWithSlash() {
        Optional<ValidationError> error = uriConfig.validate(uriConfig("/a/b"));
        ASSERT.that(error).hasValue(new ValidationError(uriConfig.name(), "Relative uri should not start with a '/'"));
    }

    @Test
    public void shouldReturnUri() {
        TaskConfig taskConfig = uriConfig("google.com");
        UriConfig uriConfig = ConfigElement.uriConfig.from(taskConfig);
        ASSERT.that(uriConfig.uri()).is("google.com");
    }

    private TaskConfig uriConfig(String value) {
        TaskConfig mock = mock(TaskConfig.class);
        when(mock.getValue("uri")).thenReturn(value);
        return mock;
    }
}