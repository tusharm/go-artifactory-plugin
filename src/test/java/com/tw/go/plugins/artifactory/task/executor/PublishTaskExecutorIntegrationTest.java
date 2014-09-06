package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.webstub.StubServerFacade;
import com.thoughtworks.webstub.dsl.HttpDsl;
import com.tw.go.plugins.artifactory.model.GoArtifactFactory;
import com.tw.go.plugins.artifactory.model.GoBuildDetailsFactory;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import com.tw.go.plugins.artifactory.task.publish.BuildArtifactPublisher;
import com.tw.go.plugins.artifactory.testutils.FilesystemUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;

import static com.thoughtworks.webstub.StubServerFacade.newServer;
import static com.thoughtworks.webstub.dsl.builders.ResponseBuilder.response;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.delete;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.read;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.path;
import static com.tw.go.plugins.artifactory.testutils.MapBuilder.map;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang.StringUtils.join;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.truth0.Truth.ASSERT;

public class PublishTaskExecutorIntegrationTest {
    private static StubServerFacade server;
    private static HttpDsl artifactoryStub;
    private PublishTaskExecutor executor;
    private String pluginDirectory;

    @BeforeClass
    public static void beforeAll() throws IOException {
        server = newServer(8888);
        artifactoryStub = server.withContext("/");
        server.start();
    }

    @Before
    public void beforeEach() {
        artifactoryStub.reset();
        artifactoryStub.get("/api/system/version").returns(response(200).withContent("{ \"version\" : \"3.2.1.1\" }"));

        executor = new PublishTaskExecutor(new GoArtifactFactory(), new GoBuildDetailsFactory(), new BuildArtifactPublisher());
        pluginDirectory = path(System.getProperty("java.io.tmpdir"), "com.tw.go.plugins.go-artifactory-plugin");
    }

    @Test
    public void shouldUploadArtifactAndBuildDetails() throws IOException {
        TaskConfig config = new TaskConfigBuilder()
                .uri("test-repo/path/to/artifact.ext")
                .path("src/test/resources/artifact.txt")
                .property("a", "b")
                .build();

        TaskExecutionContext executionContext =
                new TaskExecutionContextBuilder()
                        .withWorkingDir(System.getProperty("java.io.tmpdir"))
                        .withEnvVars(map("ARTIFACTORY_URL", "http://localhost:8888")
                                .and("ARTIFACTORY_USER", "admin")
                                .and("ARTIFACTORY_PASSWORD", "password")
                                .and("GO_SERVER_URL", "http://localhost:8153/go")
                                .and("GO_PIPELINE_NAME", "pipeline")
                                .and("GO_PIPELINE_COUNTER", "1")
                                .and("GO_STAGE_COUNTER", "3"))
                        .build();

        File testResponse = new File(path("src", "test", "resources", "uploadResponse.json"));
        artifactoryStub.put("/test-repo/path/to/artifact.ext").withContent("content")
                .returns(response(201).withContent(read(testResponse)));

        artifactoryStub.put("/test-repo/path/to/artifact.ext.sha1").withContent("040f06fd774092478d450774f5ba30c5da78acc8").returns(response(201));
        artifactoryStub.put("/test-repo/path/to/artifact.ext.md5").withContent("9a0364b9e99bb480dd25e1f0284c8555").returns(response(201));
        artifactoryStub.put("/api/build").withHeader("Content-Type", "application/vnd.org.jfrog.artifactory+json").returns(response(204));

        ExecutionResult result = executor.execute(config, executionContext);

        ASSERT.that(result.isSuccessful()).isTrue();
        ASSERT.that(new File(pluginDirectory, "uploadMetadata.json").exists()).isTrue();
    }

    @After
    public void afterEach() throws IOException {
        delete(new File(pluginDirectory));
    }

    @AfterClass
    public static void afterAll() {
        server.stop();
    }

    // Helpful for debugging
    private void enableHttpLogs() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "ERROR");
    }
}