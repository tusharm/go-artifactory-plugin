package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;

public class PublishTaskExecutor implements TaskExecutor {
    private String urlKey;
    private String pathKey;

    public PublishTaskExecutor(String urlKey, String pathKey) {
        this.urlKey = urlKey;
        this.pathKey = pathKey;
    }

    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        context.console().printLine("Artifactory URL : " + config.getValue(urlKey));
        context.console().printLine("Path : " + config.getValue(pathKey));

        return ExecutionResult.success("Finished running Artifactory plugin!");
    }
}
