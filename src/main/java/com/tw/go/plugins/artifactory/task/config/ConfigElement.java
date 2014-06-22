package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;

public abstract class ConfigElement<T> {
    public static ConfigElement<String> uri = new UriConfigElement();
    public static ConfigElement<String> path = new PathConfigElement();
    public static ConfigElement<Map<String, String>> properties = new BuildPropertiesConfigElement();

    private String name;
    private String validationErrorMessage;

    protected ConfigElement(String name, String validationErrorMessage) {
        this.name = name;
        this.validationErrorMessage = validationErrorMessage;
    }

    public String name() {
        return name;
    }

    public Optional<ValidationError> validate(String value) {
        return fromNullable(isValid(value) ? null : new ValidationError(name(), validationErrorMessage));
    }

    protected abstract boolean isValid(String value);

    public abstract T from(TaskConfig taskConfig);
}
