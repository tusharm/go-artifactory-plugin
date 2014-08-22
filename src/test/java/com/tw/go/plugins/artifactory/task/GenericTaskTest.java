package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import org.junit.Test;

import java.util.List;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.truth0.Truth.ASSERT;

public class GenericTaskTest {
    private ConfigElement configElement = mock(ConfigElement.class);
    private GenericTask task = new TestableGenericTask(asList(configElement));

    @Test
    public void shouldAddConfigElementToTaskConfig() {
        TaskConfig config = task.config();
        verify(configElement).addTo(config);
    }

    @Test
    public void shouldReturnErrorsIfValidationFails() {
        TaskConfig taskConfig = mock(TaskConfig.class);
        ValidationError error = new ValidationError("error message");

        when(configElement.validate(taskConfig)).thenReturn(of(error));

        ValidationResult result = task.validate(taskConfig);

        ASSERT.that(result.getErrors()).has().exactly(error);
    }

    @Test
    public void shouldNotReturnErrorsIfValidationPasses() {
        TaskConfig taskConfig = mock(TaskConfig.class);
        when(configElement.validate(taskConfig)).thenReturn(absent());

        ValidationResult result = task.validate(taskConfig);

        ASSERT.that(result.isSuccessful()).isTrue();
    }

    private static class TestableGenericTask extends GenericTask {
        public TestableGenericTask(List<? extends ConfigElement> configElements) {
            super(configElements);
        }

        @Override
        protected String taskViewName() {
            return null;
        }

        @Override
        protected String taskDisplayName() {
            return null;
        }

        @Override
        public TaskExecutor executor() {
            return null;
        }
    }
}