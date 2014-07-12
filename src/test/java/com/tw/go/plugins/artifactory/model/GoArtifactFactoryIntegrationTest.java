package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import com.tw.go.plugins.artifactory.task.executor.TaskExecutionContextBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collection;

import static org.truth0.Truth.ASSERT;

public class GoArtifactFactoryIntegrationTest {
    private static GoArtifactFactory factory;
    private static TaskExecutionContext context;

    @BeforeClass
    public static void beforeAll() throws Exception {
        context = new TaskExecutionContextBuilder()
                .withWorkingDir(System.getProperty("user.dir"))
                .build();
        factory = new GoArtifactFactory();
    }

    @Test
    public void shouldCreateGoArtifacts() {
        TaskConfig config = new TaskConfigBuilder()
                .path(path("src", "test", "resources", "artifact.txt"))
                .uri("repo/path/to/artifact.ext")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact expectedArtifact = new GoArtifact(
                path(System.getProperty("user.dir"), "src", "test", "resources", "artifact.txt"),
                "repo/path/to/artifact.ext"
        );
        ASSERT.that(artifacts).has().exactly(expectedArtifact);
    }

    @Test
    public void shouldCreateArtifactsWithUniqueUris() {
        TaskConfig config = new TaskConfigBuilder()
                .path(path("src", "test", "resources", "**{artifact.txt,test.html}"))
                .uri("repo/path")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact artifactTxt = new GoArtifact(
                path(System.getProperty("user.dir"), "src", "test", "resources", "artifact.txt"),
                "repo/path/artifact.txt"
        );
        GoArtifact testHtml = new GoArtifact(
                path(System.getProperty("user.dir"), "src", "test", "resources", "view", "test.html"),
                "repo/path/test.html"
        );

        ASSERT.that(artifacts).has().exactly(artifactTxt, testHtml);
    }

    private String path(String first, String... more) {
        return Paths.get(first, more).toString();
    }
}