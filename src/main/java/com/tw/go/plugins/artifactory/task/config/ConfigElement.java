package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;

import java.io.File;

import static com.google.common.base.Optional.fromNullable;

public enum ConfigElement {
    url("Invalid HTTP URL") {
        @Override
        public boolean isValid(String value) {
            return value.matches("http(s)?://.*");
        }
    },
    path("Path should be relative to workspace") {
        @Override
        public boolean isValid(String value) {
            return !value.startsWith(File.separator);
        }
    };

    private String validationErrorMessage;

    ConfigElement(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    public Optional<ValidationError> validate(String value) {
        return fromNullable(isValid(value) ? null : new ValidationError(name(), validationErrorMessage));
    }

    public abstract boolean isValid(String value);
}
