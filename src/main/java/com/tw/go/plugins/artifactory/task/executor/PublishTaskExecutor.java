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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.failure;
import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.success;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.*;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static java.lang.String.format;

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
        Collection<GoArtifact> artifacts = artifactFactory.createArtifacts(config, context);

        EnvironmentVariables environment = context.environment();
        GoBuildDetails details = buildDetailsFactory.createBuildDetails(buildProperties.from(config), environment, artifacts);

        try (ArtifactoryClient client = createClient(environment)) {
            client.uploadArtifacts(artifacts);
            client.uploadBuildDetails(details);

            return success(format("Successfully published artifact [%s]", artifacts));
        }
        catch (IOException|NoSuchAlgorithmException e) {
            String message = format("Failed to publish one or more artifact [%s]", artifacts);
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
