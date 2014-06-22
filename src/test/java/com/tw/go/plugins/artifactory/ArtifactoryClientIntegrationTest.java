package com.tw.go.plugins.artifactory;

import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.truth0.Truth.ASSERT;

public class ArtifactoryClientIntegrationTest {

    private ArtifactoryBuildInfoClient buildInfoClient;
    private ArtifactoryClient client;

    @Before
    public void beforeEach() {
        buildInfoClient = mock(ArtifactoryBuildInfoClient.class);
        client = new ArtifactoryClient(buildInfoClient);
    }

    @Test
    public void shouldUploadAnArtifact() throws IOException {
        String sourcePath = System.getProperty("user.dir") + "/src/test/resources/artifact.txt";
        client.uploadArtifact(sourcePath, "repo/path/to/artifact.txt");

        ArgumentCaptor<DeployDetails> captor = ArgumentCaptor.forClass(DeployDetails.class);
        verify(buildInfoClient).deployArtifact(captor.capture());

        DeployDetails deployDetails = captor.getValue();
        ASSERT.that(deployDetails.getTargetRepository()).is("repo");
        ASSERT.that(deployDetails.getArtifactPath()).is("path/to/artifact.txt");
        ASSERT.that(deployDetails.getFile().getAbsolutePath()).is(sourcePath);
    }

    @Test
    public void shouldCleanlyShutdown() throws IOException {
        client.close();
        verify(buildInfoClient).shutdown();
    }
}