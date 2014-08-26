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
    public void shouldInvalidateAnEmptyUri() {
        Optional<ValidationError> error = uriConfig.validate(config("", true));
        ASSERT.that(error).hasValue(new ValidationError("URI", "Uri is mandatory"));
    }

    @Test
    public void shouldInvalidateUriStartingWithSlash() {
        Optional<ValidationError> error = uriConfig.validate(config("/a/b", true));
        ASSERT.that(error).hasValue(new ValidationError("URI", "Relative uri should not start with a '/'"));
    }

    @Test
    public void shouldReturnUriWithTrailingSlashesRemoved() {
        TaskConfig taskConfig = config("google.com", true);
        ASSERT.that(uriConfig.from(taskConfig).uri()).is("google.com");
    }

    @Test
    public void shouldReturnWhetherUriDenotesFolder() {
        TaskConfig taskConfig = config("/path/to/artifact.ext", false);
        ASSERT.that(uriConfig.from(taskConfig).isFolder()).is(false);
    }

    private TaskConfig config(String value, boolean isFolder) {
        TaskConfig mock = mock(TaskConfig.class);

        when(mock.getValue("URI")).thenReturn(value);
        when(mock.getValue("uriIsFolder")).thenReturn(String.valueOf(isFolder));

        return mock;
    }
}