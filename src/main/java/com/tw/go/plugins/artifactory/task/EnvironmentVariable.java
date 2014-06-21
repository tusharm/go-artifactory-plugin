package com.tw.go.plugins.artifactory.task;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;

import java.util.Map;

import static com.google.common.base.Optional.fromNullable;

public enum EnvironmentVariable {
    ARTIFACTORY_URL, ARTIFACTORY_USER, ARTIFACTORY_PASSWORD;

    public Optional<String> from(EnvironmentVariables variables) {
        Map<String, String> envMap = variables.asMap();
        return fromNullable(envMap.get(name()));
    }
}
