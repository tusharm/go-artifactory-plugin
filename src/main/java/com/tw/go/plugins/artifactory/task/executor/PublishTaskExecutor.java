package com.tw.go.plugins.artifactory.task.executor;

import com.google.common.base.Splitter;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import org.jfrog.build.api.util.NullLog;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;

import java.io.File;
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
        try {
            EnvironmentVariables environment = context.environment();
            String url = ARTIFACTORY_URL.from(environment).get();
            String user = ARTIFACTORY_USER.from(environment).get();
            String password = ARTIFACTORY_PASSWORD.from(environment).get();
            ArtifactoryBuildInfoClient client = new ArtifactoryBuildInfoClient(url, user, password, new NullLog());

            List<String> uriSegments = toRepoAndArtifactPath(config.getValue(uriKey));
            String repository = uriSegments.get(0);
            String artifactPath = uriSegments.get(1);

            File artifactFile = new File(context.workingDir() + File.separator + config.getValue(pathKey));

            DeployDetails deployDetails = new DeployDetails.Builder().targetRepository(repository).artifactPath(artifactPath).file(artifactFile).build();
            client.deployArtifact(deployDetails);

            return ExecutionResult.success(format("Published artifact [%s]", artifactPath));
        } catch (Exception e) {
            String message = format("Error publishing artifact. Root cause: %s", e.getMessage());

            logger.error(format("%s\n%s", message, toString(e)));
            return ExecutionResult.failure(message);
        }
    }

    private List<String> toRepoAndArtifactPath(String uri) {
        return Splitter.on("/").limit(2).splitToList(uri);
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
