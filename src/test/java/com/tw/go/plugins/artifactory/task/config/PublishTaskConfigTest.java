package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PublishTaskConfigTest {
    private PublishTaskConfig configuration;

    @Before
    public void beforeEach() throws Exception {
        configuration = new PublishTaskConfig();
    }

    @Test
    public void shouldReturnTaskConfigWithUrlProperty() {
        TaskConfig taskConfig = configuration.get();

        assertThat(taskConfig.get("url"), notNullValue());
        assertThat(taskConfig.get("url").getValue(), is(nullValue()));
    }

    @Test
    public void shouldValidateUrlPropertyInTaskConfig() {
        TaskConfig invalidUrlConfig = taskConfig("url", "not-a-valid-url");
        TaskConfig httpUrlConfig = taskConfig("url", "http://localhost:8081/artifactory");
        TaskConfig httpsUrlConfig = taskConfig("url", "https://localhost:8081");

        assertValidationErrors(configuration.validate(invalidUrlConfig), new ValidationError("url", "Invalid HTTP URL"));
        assertThat(configuration.validate(httpUrlConfig).isSuccessful(), is(true));
        assertThat(configuration.validate(httpsUrlConfig).isSuccessful(), is(true));
    }

    private void assertValidationErrors(ValidationResult result, ValidationError... expectedErrors) {
        List<ValidationError> actualErrors = result.getErrors();

        assertThat(actualErrors.size(), is(expectedErrors.length));
        assertThat(actualErrors, hasItems(expectedErrors));
    }

    private TaskConfig taskConfig(String name, String value) {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.add(new TaskConfigProperty(name, value));
        return taskConfig;
    }
}