package com.tw.go.plugins.artifactory.task;

import static java.lang.String.format;

public class EnvironmentVariableMissingException extends RuntimeException {
    public EnvironmentVariableMissingException(String variableName) {
        super(format("Environment variable [%s] not found", variableName));
    }
}
