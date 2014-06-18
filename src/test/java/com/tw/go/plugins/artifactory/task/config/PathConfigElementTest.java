package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import org.junit.Test;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static org.truth0.Truth.ASSERT;

public class PathConfigElementTest {
    @Test
    public void shouldBeARelativePath() {
        Optional<ValidationError> error = path.validate("a/b");
        ASSERT.that(error).isAbsent();

        error = path.validate("../a/b");
        ASSERT.that(error).isAbsent();

        error = path.validate("/a/b");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));

        error = path.validate("/a/b");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));
    }

    @Test
    public void shouldNotValidateAnAbsolutePath() {
        Optional<ValidationError> error = path.validate("/a/b");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));

        error = path.validate("E:\\b");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));
    }

    @Test
    public void shouldNotValidateEmptyPath() {
        Optional<ValidationError> error = path.validate("");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));
    }
}
