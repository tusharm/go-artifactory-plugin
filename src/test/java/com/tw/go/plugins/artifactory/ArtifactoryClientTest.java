package com.tw.go.plugins.artifactory;

import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Artifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.util.FileChecksumCalculator;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.truth0.Truth.ASSERT;

public class ArtifactoryClientTest {

    private ArtifactoryBuildInfoClient buildInfoClient;
    private ArtifactoryClient client;


    @Before
    public void beforeEach() {
        buildInfoClient = mock(ArtifactoryBuildInfoClient.class);
        client = new ArtifactoryClient(buildInfoClient);
    }

    @Test
    public void shouldUploadAnArtifact() throws IOException, NoSuchAlgorithmException {
        String sourcePath = System.getProperty("user.dir") + "/src/test/resources/artifact.txt";

        GoArtifact artifact = new GoArtifact(sourcePath, "repo/path/to/artifact.txt");

        client.uploadArtifacts(asList(artifact));

        ArgumentCaptor<DeployDetails> captor = ArgumentCaptor.forClass(DeployDetails.class);
        verify(buildInfoClient).deployArtifact(captor.capture());

        DeployDetails deployDetails = captor.getValue();

        ASSERT.that(deployDetails.getTargetRepository()).is("repo");
        ASSERT.that(deployDetails.getArtifactPath()).is("path/to/artifact.txt");
        ASSERT.that(deployDetails.getFile().getAbsolutePath()).is(sourcePath);
        ASSERT.that(deployDetails.getSha1()).is("040f06fd774092478d450774f5ba30c5da78acc8");
        ASSERT.that(deployDetails.getMd5()).is("9a0364b9e99bb480dd25e1f0284c8555");
    }

    @Test
    public void shouldUploadBuildDetails() throws IOException {
        Properties buildProperties = new Properties();
        buildProperties.put("a", "b");

        GoArtifact artifact = new GoArtifact("/a/b", "c/d");

        GoBuildDetails details = new GoBuildDetailsBuilder()
                .buildName("buildName")
                .buildNumber("1.2")
                .startedAt(new DateTime(2004, 12, 13, 21, 39, 45, 618, DateTimeZone.forID("Asia/Kolkata")))
                .artifact(artifact)
                .properties(buildProperties)
                .url("http://google.com")
                .build();

        client.uploadBuildDetails(details);

        ArgumentCaptor<Build> captor = ArgumentCaptor.forClass(Build.class);
        verify(buildInfoClient).sendBuildInfo(captor.capture());

        Build build = captor.getValue();
        ASSERT.that(build.getName()).is("buildName");
        ASSERT.that(build.getUrl()).is("http://google.com");
        ASSERT.that(build.getNumber()).is("1.2");
        ASSERT.that(build.getStarted()).is("2004-12-13T21:39:45.618+0530");
        ASSERT.that(build.getModules().size()).is(1);
        ASSERT.that(build.getProperties()).is(buildProperties);


        List<Artifact> artifacts = build.getModule("buildName").getArtifacts();
        ASSERT.that(artifacts).isNotEmpty();
        ASSERT.that(artifacts.get(0).getName()).is("/a/b");
    }

    @Test
    public void shouldCleanlyShutdown() throws IOException {
        client.close();
        verify(buildInfoClient).shutdown();
    }
}