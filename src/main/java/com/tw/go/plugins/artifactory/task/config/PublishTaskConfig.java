package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

public class PublishTaskConfig {

    public TaskConfig get() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addProperty("url");
        return taskConfig;
    }

    public ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult result = new ValidationResult();

        if (!taskConfig.getValue("url").matches("http(s)?://.*")) {
            result.addError(new ValidationError("url", "Invalid HTTP URL"));
        }
        return result;
    }
}
