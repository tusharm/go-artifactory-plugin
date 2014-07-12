package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import org.junit.Test;

import java.util.Map;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.properties;
import static org.truth0.Truth.ASSERT;

public class BuildPropertiesConfigElementTest {
    @Test
    public void shouldValidateKeyValuePairsSeparatedByNewline() {
        ASSERT.that(properties.validate("a=b")).isAbsent();
        ASSERT.that(properties.validate("a=b\nc=d")).isAbsent();
        ASSERT.that(properties.validate("a=b\n\n\nc=d\n\n")).isAbsent();
    }

    @Test
    public void shouldNotValidateIfNotCorrectlyFormatted() {
        ASSERT.that(properties.validate("a=b=c")).hasValue(new ValidationError("properties", "Invalid properties format"));
    }

    @Test
    public void shouldReturnPropertiesAsMap() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.add(new TaskConfigProperty("properties", "a=b\n\nc=d \n\n\n"));
        Map<String, String> propertiesMap = properties.from(taskConfig);

        ASSERT.that(propertiesMap).hasKey("a").withValue("b");
        ASSERT.that(propertiesMap).hasKey("c").withValue("d");
    }
}
