package com.tw.go.plugins.artifactory.task;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskView;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import com.tw.go.plugins.artifactory.task.view.TemplateBasedTaskView;

import java.util.List;

public abstract class GenericTask implements Task {
    private List<ConfigElement<?>> configs;

    public GenericTask(List<ConfigElement<?>> configs) {
        this.configs = configs;
    }

    @Override
    public final TaskConfig config() {
        TaskConfig taskConfig = new TaskConfig();

        for (ConfigElement config : configs) {
            config.addTo(taskConfig);
        }
        return taskConfig;
    }

    @Override
    public final ValidationResult validate(TaskConfig taskConfig) {
        ValidationResult result = new ValidationResult();

        for (ConfigElement config : configs) {
            Optional<ValidationError> error = config.validate(taskConfig);
            if (error.isPresent()) {
                result.addError(error.get());
            }
        }
        return result;
    }

    @Override
    public TaskView view() {
        return new TemplateBasedTaskView(taskDisplayName(), taskViewName());
    }

    protected abstract String taskViewName();

    protected abstract String taskDisplayName();
}
