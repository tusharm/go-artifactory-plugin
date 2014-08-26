package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.io.File.separator;

public class PathConfigElement extends ConfigElement<String> {
    private static final String PATH = "Path";

    protected PathConfigElement() {
        super(PATH);
    }

    @Override
    public Optional<ValidationError> validate(TaskConfig taskConfig) {
        String value = from(taskConfig);

        if (value.isEmpty())
            return of(new ValidationError(PATH, "Path is mandatory"));

        if (isAbsolutePath(value))
            return of(new ValidationError(PATH, "Path should be relative to workspace"));

        return absent();
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(PATH);
    }

    private boolean isAbsolutePath(String pathString) {
        return pathString.startsWith(separator) || pathString.matches("\\w:.*");
    }
}
