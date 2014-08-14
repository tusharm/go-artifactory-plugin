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
    public static ConfigElement<Map<String, String>> buildProperties = new BuildPropertiesConfigElement();

    private String name;

    protected ConfigElement(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void addPropertyTo(TaskConfig taskConfig) {
        taskConfig.addProperty(name());
    }

    public Optional<ValidationError> validate(TaskConfig taskConfig) {
        String configValue = taskConfig.getValue(name());
        return validate(configValue);
    }

    public abstract Optional<ValidationError> validate(String value);
    public abstract T from(TaskConfig taskConfig);
}
