package com.tw.go.plugins.artifactory.client;

import com.google.common.collect.Iterables;
import com.tw.go.plugins.artifactory.GoBuildDetailsBuilder;
import com.tw.go.plugins.artifactory.model.ArtifactUploadMetadata;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.Module;
import org.jfrog.build.api.builder.ArtifactBuilder;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.api.builder.ModuleBuilder;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.ArtifactoryUploadResponse;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static com.google.common.collect.Iterables.getFirst;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.path;
import static com.tw.go.plugins.artifactory.testutils.MapBuilder.map;
import static com.tw.go.plugins.artifactory.testutils.matchers.DeepEqualsMatcher.deepEquals;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.jfrog.build.api.BuildInfoProperties.BUILD_INFO_ENVIRONMENT_PREFIX;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class ArtifactoryClientTest {

    @Mock
    private ArtifactoryBuildInfoClient buildInfoClient;

    private ArtifactoryClient client;
    private GoArtifact artifact;
    private ArtifactoryUploadResponse stubResponse;

    @Captor
    private ArgumentCaptor<DeployDetails> deployDetailsCaptor;

    @Before
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);

        client = new ArtifactoryClient(buildInfoClient);

        String sourcePath = path(System.getProperty("user.dir"), "src", "test", "resources", "artifact.txt");
        artifact = new GoArtifact(sourcePath, "repo/path/to/artifact.txt");
        artifact.properties(map("name", "value"));

        stubResponse = new ArtifactoryUploadResponseBuilder().build();
    }

    @Test
    public void shouldUploadAnArtifact() throws IOException, NoSuchAlgorithmException {
        when(buildInfoClient.deployArtifact(any(DeployDetails.class))).thenReturn(stubResponse);

        Collection<ArtifactUploadMetadata> uploadMetadata = client.uploadArtifacts(asList(artifact));

        verify(buildInfoClient).deployArtifact(deployDetailsCaptor.capture());
        assertThat(deployDetailsCaptor.getValue(), deepEquals(new DeployDetails.Builder()
                                .targetRepository("repo")
                                .artifactPath("path/to/artifact.txt")
                                .file(new File(artifact.localPath()))
                                .md5("9a0364b9e99bb480dd25e1f0284c8555")
                                .sha1("040f06fd774092478d450774f5ba30c5da78acc8")
                                .addProperty("name", "value")
                                .build()
                )
        );

        assertThat(uploadMetadata.size(), is(1));
        assertMatches(getFirst(uploadMetadata, null), stubResponse);
    }

    @Test
    public void shouldRemoveTrailingSlashesFromBuildPropertyValues() throws IOException {
        artifact.properties(map("url", "http://a.com/b/"));
        client.uploadArtifacts(asList(artifact));

        verify(buildInfoClient).deployArtifact(deployDetailsCaptor.capture());

        DeployDetails deployDetails = deployDetailsCaptor.getValue();
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

    private void assertMatches(ArtifactUploadMetadata metadata, ArtifactoryUploadResponse response) {
        assertThat(metadata.getRepo(), is(response.getRepo()));
        assertThat(metadata.getPath(), is(response.getPath()));
        assertThat(metadata.getCreated(), is(response.getCreated()));
        assertThat(metadata.getCreatedBy(), is(response.getCreatedBy()));
        assertThat(metadata.getDownloadUri(), is(response.getDownloadUri()));
        assertThat(metadata.getMimeType(), is(response.getMimeType()));
        assertThat(metadata.getSize(), is(response.getSize()));
        ASSERT.that(metadata.getErrors()).has().exactly("[status] message");
        assertThat(metadata.getChecksums(), is("SHA1 => [a], MD5 => [b]"));
        assertThat(metadata.getOriginalChecksums(), is("SHA1 => [c], MD5 => [d]"));
    }
}