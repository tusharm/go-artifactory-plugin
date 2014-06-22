package com.tw.go.plugins.artifactory;

import com.google.common.base.Splitter;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ArtifactoryClient implements Closeable {
    private Logger logger = Logger.getLogger(getClass());

    private ArtifactoryBuildInfoClient buildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.buildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, logger);
    }

    public ArtifactoryClient(ArtifactoryBuildInfoClient buildInfoClient) {
        this.buildInfoClient = buildInfoClient;
    }

    public void uploadArtifact(String sourcePath, String destinationUri) throws IOException {
        List<String> uriSegments = Splitter.on("/").limit(2).splitToList(destinationUri);

        DeployDetails deployDetails = new DeployDetails.Builder()
                .targetRepository(uriSegments.get(0))
                .artifactPath(uriSegments.get(1))
                .file(new File(sourcePath))
                .build();

        buildInfoClient.deployArtifact(deployDetails);
    }

    @Override
    public void close() throws IOException {
        buildInfoClient.shutdown();
    }
}
