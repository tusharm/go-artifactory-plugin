package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.ArtifactoryClient;
import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.failure;
import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.success;
import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.*;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.properties;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;
import static java.lang.String.format;

public class PublishTaskExecutor implements TaskExecutor {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        EnvironmentVariables environment = context.environment();
        String url = ARTIFACTORY_URL.from(environment);
        String user = ARTIFACTORY_USER.from(environment);
        String password = ARTIFACTORY_PASSWORD.from(environment);

        String artifactPath = path.from(config);
        String artifactUri = uri.from(config);
        Map<String, String> buildProperties = properties.from(config);

        try (ArtifactoryClient client = new ArtifactoryClient(url, user, password)) {
            client.uploadArtifact(context.workingDir() + File.separator + artifactPath, artifactUri, buildProperties);
        }
        catch (IOException e) {
            String message = format("Failed to publish artifact [%s]", artifactPath);
            logger.error(message, e);
            return failure(format("%s: %s", message, e.getMessage()));
        }

        return success(format("Successfully published artifact [%s]", artifactPath));
    }
}
