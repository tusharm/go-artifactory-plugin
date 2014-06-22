package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.webstub.StubServerFacade;
import com.thoughtworks.webstub.dsl.HttpDsl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.webstub.StubServerFacade.newServer;
import static com.thoughtworks.webstub.dsl.builders.ResponseBuilder.response;
import static java.lang.String.format;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class PublishTaskExecutorIntegrationTest {
    private static StubServerFacade server;
    private static HttpDsl artifactoryStub;
    private PublishTaskExecutor executor;

    @BeforeClass
    public static void beforeAll() {
        server = newServer(8888);
        artifactoryStub = server.withContext("/");
        server.start();
    }

    @Before
    public void beforeEach() {
        executor = new PublishTaskExecutor();
    }

    @Test
    public void shouldUploadAnArtifact() {
        Map<String, String> buildProperties = new HashMap() {{ put("a", "b"); }};
        TaskConfig config = taskConfig("test-repo/path/to/artifact.ext", "src/test/resources/artifact.txt", buildProperties);

        TaskExecutionContext executionContext =
                new TaskExecutionContextBuilder().withWorkingDir(System.getProperty("user.dir"))
                .withEnvVar("ARTIFACTORY_URL", "http://localhost:8888")
                .withEnvVar("ARTIFACTORY_USER", "admin")
                .withEnvVar("ARTIFACTORY_PASSWORD", "password")
                .build();

        artifactoryStub.put("/test-repo/path/to/artifact.ext").withContent("content").returns(response(201));
        artifactoryStub.put("/test-repo/path/to/artifact.ext.sha1").withContent("040f06fd774092478d450774f5ba30c5da78acc8").returns(response(201));
        artifactoryStub.put("/test-repo/path/to/artifact.ext.md5").withContent("9a0364b9e99bb480dd25e1f0284c8555").returns(response(201));

        ExecutionResult result = executor.execute(config, executionContext);

        ASSERT.that(result.isSuccessful()).isTrue();
    }

    @AfterClass
    public static void afterAll() {
        server.stop();
    }

    private TaskConfig taskConfig(String artifactUri, String artifactPath, Map<String, String> buildProperties) {
        TaskConfig config = mock(TaskConfig.class);
        when(config.getValue("uri")).thenReturn(artifactUri);
        when(config.getValue("path")).thenReturn(artifactPath);

        StringBuilder props = new StringBuilder();
        for (String key : buildProperties.keySet()) {
            props.append(format("%s=%s\n", key, buildProperties.get(key)));
        }
        when(config.getValue("properties")).thenReturn(props.toString());
        return config;
    }
}