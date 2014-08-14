package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static java.util.Arrays.asList;

public abstract class ConfigElement<T> {
    public static ConfigElement<String> uri = new UriConfigElement();
    public static ConfigElement<String> path = new PathConfigElement();
    public static ConfigElement<Map<String, String>> buildProperties = new BuildPropertiesConfigElement();

    public List<String> names;

    protected ConfigElement(String... name) {
        this.names = asList(name);
    }

    public String name() {
        return names.get(0);
    }

    public void addTo(TaskConfig taskConfig) {
        for (String name : names) {
            taskConfig.addProperty(name);
        }
    }

    public abstract T from(TaskConfig taskConfig);
    public abstract Optional<ValidationError> validate(TaskConfig taskConfig);
}
