package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.ArtifactoryClient;
import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoArtifactFactory;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import com.tw.go.plugins.artifactory.model.GoBuildDetailsFactory;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;

import java.io.IOException;

import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.failure;
import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.success;
import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.*;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.properties;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class PublishTaskExecutor implements TaskExecutor {
    private Logger logger = Logger.getLogger(getClass());
    private GoBuildDetailsFactory buildDetailsFactory;
    private GoArtifactFactory artifactFactory;

    public PublishTaskExecutor(GoArtifactFactory factory, GoBuildDetailsFactory buildDetailsFactory) {
        this.artifactFactory = factory;
        this.buildDetailsFactory = buildDetailsFactory;
    }

    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        EnvironmentVariables environment = context.environment();

        GoArtifact artifact = artifactFactory.createArtifact(config, context);
        GoBuildDetails details = buildDetailsFactory.createBuildDetails(properties.from(config), environment, asList(artifact));

        try (ArtifactoryClient client = createClient(environment)) {
            client.uploadArtifact(artifact);
            client.uploadBuildDetails(details);

            return success(format("Successfully published artifact [%s]", artifact.localPath()));
        }
        catch (IOException e) {
            String message = format("Failed to publish artifact [%s]", artifact.localPath());
            logger.error(message, e);
            return failure(format("%s: %s", message, e.getMessage()));
        }
    }

    private ArtifactoryClient createClient(EnvironmentVariables environment) {
        String url = ARTIFACTORY_URL.from(environment);
        String user = ARTIFACTORY_USER.from(environment);
        String password = ARTIFACTORY_PASSWORD.from(environment);

        return new ArtifactoryClient(url, user, password);
    }

}
