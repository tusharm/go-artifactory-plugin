package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;

public enum ConfigElement {
    uri("Invalid uri") {
        @Override
        public boolean isValid(String value) {
            return value.matches("[^/].*");
        }

        @Override
        public String from(TaskConfig taskConfig) {
            return taskConfig.getValue(name());
        }
    },
    path("Path should be relative to workspace") {
        @Override
        public boolean isValid(String value) {
            if (value.isEmpty())
                return false;

            return ! (value.startsWith(File.separator) || value.matches("\\w:.*"));
        }

        @Override
        public String from(TaskConfig taskConfig) {
            return taskConfig.getValue(name());
        }
    },
    properties("Invalid properties format") {
        @Override
        public boolean isValid(String value) {
            return value.matches("[^=]+=[^=]+((\n)+[^=]+=[^=]+)*(\n)*");
        }

        @Override
        public Map<String, String> from(TaskConfig taskConfig) {
            String propertiesString = taskConfig.getValue(name());
            return Splitter.on("\n").omitEmptyStrings().withKeyValueSeparator("=").split(propertiesString);
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

    public abstract <T> T from(TaskConfig taskConfig);
}
