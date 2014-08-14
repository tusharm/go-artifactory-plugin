package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;
import java.nio.file.Paths;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;

public class PathConfigElement extends ConfigElement<String> {
    protected PathConfigElement() {
        super("path");
    }

    @Override
    public Optional<ValidationError> validate(String value) {
        if (value.isEmpty())
            return of(new ValidationError(name(), "Path is mandatory"));

        if (isAbsolutePath(value))
            return of(new ValidationError(name(), "Path should be relative to workspace"));

        return absent();
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(name());
    }

    private boolean isAbsolutePath(String pathString) {
        return pathString.startsWith(File.separator) || pathString.matches("\\w:.*");
    }
}
