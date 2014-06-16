package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

public class PublishTaskConfig {
    public static final String URL = "url";

    public TaskConfig get() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addProperty(URL);
        return taskConfig;
    }

    public ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult result = new ValidationResult();

        if (!taskConfig.getValue(URL).matches("http(s)?://.*")) {
            result.addError(new ValidationError(URL, "Invalid HTTP URL"));
        }
        return result;
    }
}
