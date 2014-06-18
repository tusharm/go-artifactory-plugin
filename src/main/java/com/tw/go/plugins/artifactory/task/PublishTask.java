package com.tw.go.plugins.artifactory.task;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import com.tw.go.plugins.artifactory.task.view.TemplateBasedTaskView;

import java.util.EnumSet;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.url;
import static java.util.EnumSet.allOf;

@Extension
public class PublishTask implements Task {
    private EnumSet<ConfigElement> configs;

    public PublishTask() {
        this(allOf(ConfigElement.class));
    }

    public PublishTask(EnumSet<ConfigElement> configs) {
        this.configs = configs;
    }

    @Override
    public TaskConfig config() {
        TaskConfig taskConfig = new TaskConfig();

        for (ConfigElement config : configs) {
            taskConfig.addProperty(config.name());
        }
        return taskConfig;
    }

    @Override
    public ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult result = new ValidationResult();

        for (ConfigElement config : configs) {
            String configValue = taskConfig.getValue(config.name());
            Optional<ValidationError> error = config.validate(configValue);

            if (error.isPresent()) {
                result.addError(error.get());
            }
        }
        return result;
    }

    @Override
    public TaskView view() {
        return new TemplateBasedTaskView("Publish to Artifactory", "publish");
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor(url.name(), path.name());
    }

}
