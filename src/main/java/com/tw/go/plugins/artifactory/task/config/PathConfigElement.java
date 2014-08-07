package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;
import java.nio.file.Paths;

public class PathConfigElement extends ConfigElement<String> {
    protected PathConfigElement() {
        super("path", "Path should be relative to workspace");
    }

    @Override
    protected boolean isValid(String value) {
        return (value.isEmpty() || isAbsolutePath(value)) ? false : true;
    }

    @Override
    public String from(TaskConfig taskConfig) {
        return taskConfig.getValue(name());
    }

    private boolean isAbsolutePath(String pathString) {
        return pathString.startsWith(File.separator) || pathString.matches("\\w:.*");
    }
}
