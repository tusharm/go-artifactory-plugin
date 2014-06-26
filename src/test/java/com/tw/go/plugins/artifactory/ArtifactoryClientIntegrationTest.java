package com.tw.go.plugins.artifactory;

import org.jfrog.build.api.Build;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.truth0.Truth;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> properties = new HashMap<String, String>() {{
            put("a", "b");
        }};

        String sourcePath = System.getProperty("user.dir") + "/src/test/resources/artifact.txt";
        client.uploadArtifact(sourcePath, "repo/path/to/artifact.txt", properties);

        ArgumentCaptor<DeployDetails> captor = ArgumentCaptor.forClass(DeployDetails.class);
        verify(buildInfoClient).deployArtifact(captor.capture());

        DeployDetails deployDetails = captor.getValue();
        ASSERT.that(deployDetails.getTargetRepository()).is("repo");
        ASSERT.that(deployDetails.getArtifactPath()).is("path/to/artifact.txt");
        ASSERT.that(deployDetails.getFile().getAbsolutePath()).is(sourcePath);
        ASSERT.that(deployDetails.getProperties()).isEqualTo(properties);
    }

    @Test
    public void shouldUploadBuildDetails() throws IOException {
        BuildDetails details = new BuildDetailsBuilder()
                .buildName("buildName")
                .buildNumber("1.2")
                .startedAt(new DateTime(2004, 12, 13, 21, 39, 45, 618, DateTimeZone.forID("Asia/Kolkata")))
                .build();
        client.uploadBuildDetails(details);

        ArgumentCaptor<Build> captor = ArgumentCaptor.forClass(Build.class);
        verify(buildInfoClient).sendBuildInfo(captor.capture());

        Build build = captor.getValue();
        ASSERT.that(build.getName()).is("buildName");
        ASSERT.that(build.getNumber()).is("1.2");
        ASSERT.that(build.getStarted()).is("2004-12-13T21:39:45.618+0530");
    }

    @Test
    public void shouldCleanlyShutdown() throws IOException {
        client.close();
        verify(buildInfoClient).shutdown();
    }
}