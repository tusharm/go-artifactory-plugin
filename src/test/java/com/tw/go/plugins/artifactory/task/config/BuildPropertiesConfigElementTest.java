package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import org.junit.Test;

import java.util.Map;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class BuildPropertiesConfigElementTest {
    @Test
    public void shouldValidateKeyValuePairsSeparatedByNewline() {
        TaskConfig taskConfig = propertiesConfig("a=b");
        ASSERT.that(buildProperties.validate(taskConfig)).isAbsent();

        taskConfig = propertiesConfig("a=b\nc=d");
        ASSERT.that(buildProperties.validate(taskConfig)).isAbsent();

        taskConfig = propertiesConfig("a=b\n\n\nc=d\n\n");
        ASSERT.that(buildProperties.validate(taskConfig)).isAbsent();
    }

    @Test
    public void shouldNotValidateIfNotCorrectlyFormatted() {
        TaskConfig taskConfig = propertiesConfig("a=b=c");

        ASSERT.that(buildProperties.validate(taskConfig))
                .hasValue(new ValidationError("Properties", "Invalid properties format"));
    }

    @Test
    public void shouldReturnPropertiesAsMap() {
        TaskConfig taskConfig = propertiesConfig("a=b\n\nc=d \n\n\n");
        Map<String, String> propertiesMap = buildProperties.from(taskConfig);

        ASSERT.that(propertiesMap).hasKey("a").withValue("b");
        ASSERT.that(propertiesMap).hasKey("c").withValue("d");
    }

    private TaskConfig propertiesConfig(String value) {
        TaskConfig mock = mock(TaskConfig.class);
        when(mock.getValue("Properties")).thenReturn(value);
        return mock;
    }
}
