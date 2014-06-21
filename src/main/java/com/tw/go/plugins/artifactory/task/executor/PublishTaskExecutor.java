package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.ArtifactoryClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class PublishTaskExecutor implements TaskExecutor {
    private Logger logger = Logger.getLoggerFor(getClass());

    private String uriKey;
    private String pathKey;

    public PublishTaskExecutor(String uriKey, String pathKey) {
        this.uriKey = uriKey;
        this.pathKey = pathKey;
    }

    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        EnvironmentVariables environment = context.environment();
        String url = ARTIFACTORY_URL.from(environment).get();
        String user = ARTIFACTORY_USER.from(environment).get();
        String password = ARTIFACTORY_PASSWORD.from(environment).get();
        ArtifactoryClient client = new ArtifactoryClient(url, user, password);

        String artifactPath = config.getValue(pathKey);
        String artifactUri = config.getValue(uriKey);

        try {
            client.uploadArtifact(context.workingDir() + File.separator + artifactPath, artifactUri);
        }
        catch (IOException e) {
            String message = format("Error publishing artifact. Root cause: %s", e.getMessage());

            logger.error(format("%s\n%s", message, toString(e)));
            return ExecutionResult.failure(message);
        }

        return ExecutionResult.success(format("Published artifact [%s]", artifactPath));
    }

    public String toString(Exception e) {
        StringBuilder stackTrace = new StringBuilder();

        List<StackTraceElement> elements = asList(e.getStackTrace());
        for (StackTraceElement element : elements) {
            stackTrace.append("\t").append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
}
