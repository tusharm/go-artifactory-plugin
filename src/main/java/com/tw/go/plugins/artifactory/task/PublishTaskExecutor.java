package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;

public class PublishTaskExecutor implements TaskExecutor {
    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        context.console().printLine("Artifactory URL : " + config.getValue(PublishTask.URL_CONFIG_NAME));
        return ExecutionResult.success("Finished running Artifactory plugin!");
    }
}
