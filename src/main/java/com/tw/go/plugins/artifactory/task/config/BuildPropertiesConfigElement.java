package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.util.Map;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;

public class BuildPropertiesConfigElement extends ConfigElement<Map<String, String>> {
    private static final String PROPERTIES_REGEX = "[^=]+=[^=]+((\\s)+[^=]+=[^=]+)*(\\s)*";

    protected BuildPropertiesConfigElement() {
        super("properties");
    }

    @Override
    public Optional<ValidationError> validate(String value) {
        if (!value.matches(PROPERTIES_REGEX))
            return of(new ValidationError(name(), "Invalid properties format"));

        return absent();
    }

    @Override
    public Map<String, String> from(TaskConfig taskConfig) {
        String propertiesString = taskConfig.getValue(name());
        return Splitter.on("\n").trimResults().omitEmptyStrings().withKeyValueSeparator("=").split(propertiesString);
    }
}
