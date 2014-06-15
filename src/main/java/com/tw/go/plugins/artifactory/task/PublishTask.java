package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.*;
import com.tw.go.plugins.artifactory.task.view.ArtifactoryTaskView;

@Extension
public class PublishTask implements Task {
    public static final String URL_CONFIG_NAME = "url";

    @Override
    public TaskConfig config() {
        TaskConfig config = new TaskConfig();
        config.addProperty(URL_CONFIG_NAME);
        return config;
    }

    @Override
    public ValidationResult validate(TaskConfig config) {
        ValidationResult result = new ValidationResult();

        String artifactorUrl = config.getValue(URL_CONFIG_NAME);
        if (! artifactorUrl.startsWith("http")) {
            result.addError(new ValidationError(URL_CONFIG_NAME, "Invalid Artifactory URL"));
        }
        return result;
    }

    @Override
    public TaskView view() {
        return new ArtifactoryTaskView("Publish to Artifactory", "publish");
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor();
    }

}
