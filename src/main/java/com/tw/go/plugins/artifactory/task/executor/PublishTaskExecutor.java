package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.ArtifactoryClient;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.failure;
import static com.thoughtworks.go.plugin.api.response.execution.ExecutionResult.success;
import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.*;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.*;
import static java.lang.String.format;

public class PublishTaskExecutor implements TaskExecutor {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        GoArtifact artifact = createArtifact(config, context.workingDir());

        EnvironmentVariables environment = context.environment();
        try (ArtifactoryClient client = createClient(environment)) {
            client.uploadArtifact(artifact);

            GoBuildDetails details = createBuildDetails(environment);
            client.uploadBuildDetails(details);
        }
        catch (IOException e) {
            String message = format("Failed to publish artifact [%s]", artifact.localPath());
            logger.error(message, e);
            return failure(format("%s: %s", message, e.getMessage()));
        }

        return success(format("Successfully published artifact [%s]", artifact.localPath()));
    }

    private GoArtifact createArtifact(TaskConfig config, String workingDir) {
        String artifactFullPath = workingDir + File.separator + path.from(config);

        GoArtifact artifact = new GoArtifact(artifactFullPath, uri.from(config));
        artifact.attachProperties(properties.from(config));

        return artifact;
    }

    private ArtifactoryClient createClient(EnvironmentVariables environment) {
        String url = ARTIFACTORY_URL.from(environment);
        String user = ARTIFACTORY_USER.from(environment);
        String password = ARTIFACTORY_PASSWORD.from(environment);

        return new ArtifactoryClient(url, user, password);
    }

    private GoBuildDetails createBuildDetails(EnvironmentVariables environment) {
        String pipeline = GO_PIPELINE_NAME.from(environment);
        String pipelineCounter = GO_PIPELINE_COUNTER.from(environment);
        String stageCounter = GO_STAGE_COUNTER.from(environment);

        String buildNumber = format("%s.%s", pipelineCounter, stageCounter);
        return new GoBuildDetails(pipeline, buildNumber, new DateTime());
    }
}
