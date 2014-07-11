package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import com.tw.go.plugins.artifactory.task.executor.TaskExecutionContextBuilder;
import org.junit.Test;

import java.io.File;

import static org.truth0.Truth.ASSERT;

public class GoArtifactFactoryTest {
    private GoArtifactFactory factory = new GoArtifactFactory();

    @Test
    public void shouldCreateGoArtifact() {
        TaskConfig config = new TaskConfigBuilder().path("path").uri("repo/path/to/artifact.ext").build();
        TaskExecutionContext context = new TaskExecutionContextBuilder().withWorkingDir("pwd").build();

        GoArtifact artifact = factory.createArtifact(config, context);

        ASSERT.that(artifact.localPath()).is("pwd" + File.separator + "path");
        ASSERT.that(artifact.repository()).is("repo");
        ASSERT.that(artifact.artifactPath()).is("path/to/artifact.ext");
    }
}