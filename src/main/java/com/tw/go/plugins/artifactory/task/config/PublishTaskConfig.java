package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import java.io.File;

import static java.lang.String.format;

public class PublishTaskConfig {
    public static final String URL = "url";
    public static final String PATH = "path";

    public TaskConfig get() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addProperty(URL);
        taskConfig.addProperty(PATH);
        return taskConfig;
    }

    public ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult result = new ValidationResult();

        if (!taskConfig.getValue(URL).matches("http(s)?://.*")) {
            result.addError(new ValidationError(URL, "Invalid HTTP URL"));
        }

        if (taskConfig.getValue(PATH).startsWith(File.separator)) {
            result.addError(new ValidationError(PATH, format("Relative path shouldn't start with %s", File.separator)));
        }

        return result;
    }
}
