package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;
import static java.util.Arrays.asList;
import static org.truth0.Truth.ASSERT;

public class GenericTaskTest {
    private GenericTask task = new PublishTask(new ArrayList<ConfigElement<?>>() {{ add(uri); }});

    @Test
    public void shouldReturnTaskConfigWithGivenConfigElements() {
        TaskConfig config = task.config();

        ASSERT.that(config.size()).isEqualTo(1);
        ASSERT.that(config.get(uri.name())).isNotNull();
    }

    @Test
    public void shouldValidateTaskConfig() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.add(new TaskConfigProperty(uri.name(), "/repo/path/to/artifact"));

        ValidationResult result = task.validate(taskConfig);
        ASSERT.that(result.getErrors()).isNotEmpty();
    }
}