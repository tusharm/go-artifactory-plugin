package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.annotation.Load;
import com.thoughtworks.go.plugin.api.info.PluginContext;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;
import com.tw.go.plugins.artifactory.task.config.PublishTaskConfig;
import com.tw.go.plugins.artifactory.task.view.TemplateBasedTaskView;

@Extension
public class PublishTask implements Task {
    public static final String URL_CONFIG_NAME = "url";
    private PublishTaskConfig publishTaskConfig;

    @Load
    public void pluginLoaded(PluginContext context) {
        publishTaskConfig = new PublishTaskConfig();
    }

    @Override
    public TaskConfig config() {
        return publishTaskConfig.get();
    }

    @Override
    public ValidationResult validate(TaskConfig config) {
        return publishTaskConfig.validate(config);
    }

    @Override
    public TaskView view() {
        return new TemplateBasedTaskView("Publish to Artifactory", "publish");
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor();
    }

}
