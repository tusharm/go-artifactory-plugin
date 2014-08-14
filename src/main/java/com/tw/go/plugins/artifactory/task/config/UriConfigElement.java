package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

public class UriConfigElement extends ConfigElement<String> {
    private static final String URI = "uri";

    protected UriConfigElement() {
        super(URI);
    }

    @Override
    public Optional<ValidationError> validate(TaskConfig taskConfig) {
        String value = from(taskConfig);

        if (value.isEmpty())
            return of(new ValidationError(URI, "Uri is mandatory"));

        if (!value.matches("[^/].*"))
            return of(new ValidationError(URI, "Relative uri should not start with a '/'"));

        return absent();
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(URI);
    }
}
