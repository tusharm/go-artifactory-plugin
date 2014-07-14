package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.webstub.StubServerFacade;
import com.thoughtworks.webstub.dsl.HttpDsl;
import com.tw.go.plugins.artifactory.model.GoArtifactFactory;
import com.tw.go.plugins.artifactory.model.GoBuildDetailsFactory;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.thoughtworks.webstub.StubServerFacade.newServer;
import static com.thoughtworks.webstub.dsl.builders.ResponseBuilder.response;
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
        artifactoryStub.reset();
        artifactoryStub.get("/api/system/version").returns(response(200).withContent("{ \"version\" : \"3.2.1.1\" }"));

        executor = new PublishTaskExecutor(new GoArtifactFactory(), new GoBuildDetailsFactory());
    }

    @Test
    public void shouldUploadArtifactAndBuildDetails() {
        TaskConfig config = new TaskConfigBuilder()
                .uri("test-repo/path/to/artifact.ext")
                .path("src/test/resources/artifact.txt")
                .property("a", "b")
                .build();

        TaskExecutionContext executionContext =
                new TaskExecutionContextBuilder().withWorkingDir(System.getProperty("user.dir"))
                        .withEnvVar("ARTIFACTORY_URL", "http://localhost:8888")
                        .withEnvVar("ARTIFACTORY_USER", "admin")
                        .withEnvVar("ARTIFACTORY_PASSWORD", "password")
                        .withEnvVar("GO_SERVER_URL", "http://localhost:8153/go")
                        .withEnvVar("GO_PIPELINE_NAME", "pipeline")
                        .withEnvVar("GO_PIPELINE_COUNTER", "1")
                        .withEnvVar("GO_STAGE_COUNTER", "3")
                        .build();

        artifactoryStub.put("/test-repo/path/to/artifact.ext").withContent("content").returns(response(201));
        artifactoryStub.put("/test-repo/path/to/artifact.ext.sha1").withContent("040f06fd774092478d450774f5ba30c5da78acc8").returns(response(201));
        artifactoryStub.put("/test-repo/path/to/artifact.ext.md5").withContent("9a0364b9e99bb480dd25e1f0284c8555").returns(response(201));

        artifactoryStub.put("/api/build").withHeader("Content-Type", "application/vnd.org.jfrog.artifactory+json").returns(response(204));

        ExecutionResult result = executor.execute(config, executionContext);

        ASSERT.that(result.isSuccessful()).isTrue();
    }

    @AfterClass
    public static void afterAll() {
        server.stop();
    }
}