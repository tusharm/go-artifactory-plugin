package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.*;
import com.tw.go.plugins.artifactory.client.ArtifactoryClient;
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
        GoBuildDetails details = buildDetailsFactory.createBuildDetails(environment, artifacts);

        Console console = context.console();
        try (ArtifactoryClient client = createClient(environment)) {
            client.uploadArtifacts(artifacts);
            client.uploadBuildDetails(details);

            // TODO: this message should be arg to success() but currently it is not working; possibly a Go fix?
            console.printLine(format("Successfully published artifacts:\n%s", asString(artifacts)));
            return success("");
        }
        catch (RuntimeException e) {
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

    private String asString(Collection<GoArtifact> artifacts) {
        StringBuilder result = new StringBuilder();
        for (GoArtifact artifact : artifacts) {
            result.append(artifact).append("\n");
        }
        return result.toString();
    }

}
