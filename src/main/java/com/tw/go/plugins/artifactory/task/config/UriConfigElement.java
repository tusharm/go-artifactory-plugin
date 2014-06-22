package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

public class UriConfigElement extends ConfigElement {
    protected UriConfigElement() {
        super("uri", "Invalid uri");
    }

    @Override
    protected boolean isValid(String value) {
        return value.matches("[^/].*");
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(name());
    }
}
