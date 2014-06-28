package com.tw.go.plugins.artifactory;

import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class ArtifactoryClient implements Closeable {
    private Logger logger = Logger.getLogger(getClass());

    private ArtifactoryBuildInfoClient buildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.buildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, logger);
    }

    public ArtifactoryClient(ArtifactoryBuildInfoClient buildInfoClient) {
        this.buildInfoClient = buildInfoClient;
    }

    public void uploadArtifact(GoArtifact artifact) throws IOException {
        DeployDetails deployDetails = new DeployDetails.Builder()
                .targetRepository(artifact.repository())
                .artifactPath(artifact.artifactPath())
                .file(new File(artifact.localPath()))
                .addProperties(artifact.properties())
                .build();

        buildInfoClient.deployArtifact(deployDetails);
    }

    public void uploadBuildDetails(GoBuildDetails details) throws IOException {
        DateTimeFormatter format = DateTimeFormat.forPattern(Build.STARTED_FORMAT);

        Build build = new BuildInfoBuilder(details.buildName())
                .number(details.buildNumber())
                .started(format.print(details.startedAt()))
                .build();

        buildInfoClient.sendBuildInfo(build);
    }

    @Override
    public void close() throws IOException {
        buildInfoClient.shutdown();
    }
}
