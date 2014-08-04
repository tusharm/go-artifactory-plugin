package com.tw.go.plugins.artifactory.task;

import static java.lang.String.format;

public class MissingEnvironmentDataException extends RuntimeException {
    public MissingEnvironmentDataException(String variableName) {
        super(format("Environment data [%s] not found", variableName));
    }
}
