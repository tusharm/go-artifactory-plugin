package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.util.Map;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

public class BuildPropertiesConfigElement extends ConfigElement<Map<String, String>> {
    private static final String PROPERTIES_REGEX = "[^=]+=[^=]+((\\s)+[^=]+=[^=]+)*(\\s)*";
    private static final String PROPERTIES = "properties";

    protected BuildPropertiesConfigElement() {
        super(PROPERTIES);
    }

    @Override
    public Optional<ValidationError> validate(TaskConfig taskConfig) {
        String value = taskConfig.getValue(PROPERTIES);

        if (!value.matches(PROPERTIES_REGEX))
            return of(new ValidationError(PROPERTIES, "Invalid properties format"));

        return absent();
    }

    @Override
    public Map<String, String> from(TaskConfig taskConfig) {
        String propertiesString = taskConfig.getValue(PROPERTIES);
        return Splitter.on("\n").trimResults().omitEmptyStrings().withKeyValueSeparator("=").split(propertiesString);
    }
}
