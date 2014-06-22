package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;

import java.util.Map;

public enum EnvironmentVariable {
    ARTIFACTORY_URL, ARTIFACTORY_USER, ARTIFACTORY_PASSWORD;

    public String from(EnvironmentVariables variables) {
        Map<String, String> envMap = variables.asMap();

        if (envMap.containsKey(name()))
            return envMap.get(name());

        throw new EnvironmentVariableMissingException(name());
    }
}
