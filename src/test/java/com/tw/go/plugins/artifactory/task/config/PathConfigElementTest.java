package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.google.common.collect.Iterables.getLast;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static org.truth0.Truth.ASSERT;

public class PathConfigElementTest {
    public static final Iterable<Path> ROOT_DIRECTORIES = FileSystems.getDefault().getRootDirectories();

    @Test
    public void shouldBeARelativePath() {
        Optional<ValidationError> error = path.validate("a/b");
        ASSERT.that(error).isAbsent();

        error = path.validate("../a/b");
        ASSERT.that(error).isAbsent();
    }

    @Test
    public void shouldNotValidateAnAbsolutePath() {
        String root = getLast(ROOT_DIRECTORIES).toString();
        Optional<ValidationError> error = path.validate(root);
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path should be relative to workspace"));
    }

    @Test
    public void shouldNotValidateEmptyPath() {
        Optional<ValidationError> error = path.validate("");
        ASSERT.that(error).hasValue(new ValidationError(path.name(), "Path is mandatory"));
    }

    @Test
    public void shouldReturnPathFromTaskConfig() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.add(new TaskConfigProperty("path", "a/b"));
        ASSERT.that(path.from(taskConfig)).is("a/b");
    }
}
