package com.tw.go.plugins.artifactory;

import com.google.common.base.Splitter;
import org.jfrog.build.api.util.NullLog;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ArtifactoryClient {
    private ArtifactoryBuildInfoClient artifactoryBuildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.artifactoryBuildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, new NullLog());
    }

    public ArtifactoryBuildInfoClient getArtifactoryBuildInfoClient() {
        return artifactoryBuildInfoClient;
    }

    public String uploadArtifact(String sourcePath, String destinationUri) throws IOException {
        List<String> uriSegments = toRepoAndArtifactPath(destinationUri);
        String repository = uriSegments.get(0);
        String artifactPath = uriSegments.get(1);

        File artifactFile = new File(sourcePath);

        DeployDetails deployDetails = new DeployDetails.Builder().targetRepository(repository).artifactPath(artifactPath).file(artifactFile).build();
        getArtifactoryBuildInfoClient().deployArtifact(deployDetails);
        return artifactPath;
    }

    private List<String> toRepoAndArtifactPath(String uri) {
        return Splitter.on("/").limit(2).splitToList(uri);
    }

}
