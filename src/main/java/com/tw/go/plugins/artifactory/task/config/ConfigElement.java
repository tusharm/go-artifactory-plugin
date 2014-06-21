package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;

import java.io.File;

import static com.google.common.base.Optional.fromNullable;

public enum ConfigElement {
    uri("Invalid uri") {
        @Override
        public boolean isValid(String value) {
            return value.matches("[^/].*");
        }
    },
    path("Path should be relative to workspace") {
        @Override
        public boolean isValid(String value) {
            if (value.isEmpty())
                return false;

            return ! (value.startsWith(File.separator) || value.matches("\\w:.*"));
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
