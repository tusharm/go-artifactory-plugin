package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.task.TaskConfig;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskConfigBuilder {

    private final TaskConfig config;

    public TaskConfigBuilder() {
        config = mock(TaskConfig.class);
    }

    public TaskConfigBuilder uri(String path) {
        when(config.getValue("URI")).thenReturn(path);
        return this;
    }

    public TaskConfigBuilder path(String path) {
        when(config.getValue("Path")).thenReturn(path);
        return this;
    }

    public TaskConfigBuilder property(String name, String value) {
        when(config.getValue("Properties")).thenReturn(format("%s=%s\n", name, value));
        return this;
    }

    public TaskConfigBuilder uriIsFolder(boolean isFolder) {
        when(config.getValue("uriIsFolder")).thenReturn(valueOf(isFolder));
        return this;
    }

    public TaskConfig build() {
        return config;
    }
}
