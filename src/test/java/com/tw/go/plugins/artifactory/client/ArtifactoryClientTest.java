package com.tw.go.plugins.artifactory.client;

import com.tw.go.plugins.artifactory.GoBuildDetailsBuilder;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Artifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.Module;
import org.jfrog.build.api.builder.ArtifactBuilder;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.api.builder.ModuleBuilder;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.tw.go.plugins.artifactory.testutils.MapBuilder.map;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.path;
import static com.tw.go.plugins.artifactory.testutils.matchers.DeepEqualsMatcher.deepEquals;
import static java.util.Arrays.asList;
import static org.jfrog.build.api.BuildInfoProperties.BUILD_INFO_ENVIRONMENT_PREFIX;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.truth0.Truth.ASSERT;

public class ArtifactoryClientTest {

    private ArtifactoryBuildInfoClient buildInfoClient;
    private ArtifactoryClient client;
    private GoArtifact artifact;

    @Before
    public void beforeEach() {
        buildInfoClient = mock(ArtifactoryBuildInfoClient.class);
        client = new ArtifactoryClient(buildInfoClient);

        String sourcePath = path(System.getProperty("user.dir"), "src", "test", "resources", "artifact.txt");
        artifact = new GoArtifact(sourcePath, "repo/path/to/artifact.txt");
        artifact.properties(map("name", "value"));
    }

    @Test
    public void shouldUploadAnArtifact() throws IOException, NoSuchAlgorithmException {

        client.uploadArtifacts(asList(artifact));

        ArgumentCaptor<DeployDetails> captor = ArgumentCaptor.forClass(DeployDetails.class);
        verify(buildInfoClient).deployArtifact(captor.capture());

        assertThat(captor.getValue(), deepEquals(new DeployDetails.Builder()
                                .targetRepository("repo")
                                .artifactPath("path/to/artifact.txt")
                                .file(new File(artifact.localPath()))
                                .md5("9a0364b9e99bb480dd25e1f0284c8555")
                                .sha1("040f06fd774092478d450774f5ba30c5da78acc8")
                                .addProperty("name", "value")
                                .build()
                )
        );
    }

    @Test
    public void shouldRemoveTrailingSlashesFromBuildPropertyValues() throws IOException {
        artifact.properties(map("url", "http://a.com/b/"));
        client.uploadArtifacts(asList(artifact));

        ArgumentCaptor<DeployDetails> captor = ArgumentCaptor.forClass(DeployDetails.class);
        verify(buildInfoClient).deployArtifact(captor.capture());

        DeployDetails deployDetails = captor.getValue();

        ASSERT.that(deployDetails.getProperties()).hasKey("url").withValue("http://a.com/b");
    }

    @Test
    public void shouldUploadBuildDetails() throws IOException {
        GoBuildDetails details = new GoBuildDetailsBuilder()
                .buildName("buildName")
                .buildNumber("1.2")
                .startedAt(new DateTime(2004, 12, 13, 21, 39, 45, 618, DateTimeZone.forID("Asia/Kolkata")))
                .artifact(artifact)
                .url("http://google.com")
                .envVariable("name", "value")
                .build();

        client.uploadBuildDetails(details);

        ArgumentCaptor<Build> captor = ArgumentCaptor.forClass(Build.class);
        verify(buildInfoClient).sendBuildInfo(captor.capture());

        Module module = new ModuleBuilder().id("buildName").addArtifact(
                        new ArtifactBuilder("artifact.txt")
                                .md5("9a0364b9e99bb480dd25e1f0284c8555")
                                .sha1("040f06fd774092478d450774f5ba30c5da78acc8")
                                .build()
                ).build();

        assertThat(captor.getValue(), deepEquals(new BuildInfoBuilder("buildName")
                                .url("http://google.com")
                                .number("1.2")
                                .started("2004-12-13T21:39:45.618+0530")
                                .modules(asList(module))
                                .addProperty(BUILD_INFO_ENVIRONMENT_PREFIX + "name", "value")
                                .build()
                )
        );
    }

    @Test
    public void shouldCleanlyShutdown() throws IOException {
        client.close();
        verify(buildInfoClient).shutdown();
    }

}