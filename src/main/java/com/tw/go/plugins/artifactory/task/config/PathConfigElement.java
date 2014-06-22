package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;

public class PathConfigElement extends ConfigElement<String> {
    protected PathConfigElement() {
        super("path", "Path should be relative to workspace");
    }

    @Override
    protected boolean isValid(String value) {
        if (value.isEmpty())
            return false;

        return ! (value.startsWith(File.separator) || value.matches("\\w:.*"));
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(name());
    }
}
