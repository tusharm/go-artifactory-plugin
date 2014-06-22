package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.util.Map;

public class BuildPropertiesConfigElement extends ConfigElement<Map<String, String>> {
    protected BuildPropertiesConfigElement() {
        super("properties", "Invalid properties format");
    }

    @Override
    protected boolean isValid(String value) {
        return value.matches("[^=]+=[^=]+((\n)+[^=]+=[^=]+)*(\n)*");
    }

    @Override
    public Map<String, String> from(TaskConfig taskConfig) {
        String propertiesString = taskConfig.getValue(name());
        return Splitter.on("\n").omitEmptyStrings().withKeyValueSeparator("=").split(propertiesString);
    }
}
