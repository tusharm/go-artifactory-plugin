package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import com.tw.go.plugins.artifactory.task.executor.TaskExecutionContextBuilder;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.truth0.Truth.ASSERT;

public class GoArtifactFactoryIntegrationTest {
    private static GoArtifactFactory factory;
    private static TaskExecutionContext context;
    private Map<String, String> properties = new HashMap<String, String>() {{ put("name", "value"); }};

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
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact expectedArtifact = goArtifact("src/test/resources/artifact.txt", "repo/path/to/artifact.ext", properties);

        ASSERT.that(artifacts).has().exactly(expectedArtifact);
    }

    @Test
    public void shouldCreateArtifactsWithUniqueUris() {
        TaskConfig config = new TaskConfigBuilder()
                .path(path("src", "test", "resources", "**{artifact.txt,test.html}"))
                .uri("repo/path")
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact artifactTxt = goArtifact("src/test/resources/artifact.txt", "repo/path/artifact.txt", properties);
        GoArtifact testHtml = goArtifact("src/test/resources/view/test.html", "repo/path/test.html", properties);

        ASSERT.that(artifacts).has().exactly(artifactTxt, testHtml);
    }

    private GoArtifact goArtifact(String localPath, String uri, Map<String, String> properties) {
        String[] segments = localPath.split("/");

        GoArtifact artifact = new GoArtifact(path(System.getProperty("user.dir"), segments), uri);
        artifact.properties(properties);
        return artifact;
    }

    private String path(String first, String... more) {
        return first + File.separator + StringUtils.join(more, File.separatorChar);
    }
}