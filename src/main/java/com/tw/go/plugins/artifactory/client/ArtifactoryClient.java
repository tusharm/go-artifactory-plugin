package com.tw.go.plugins.artifactory.client;

import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.util.FileChecksumCalculator;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ArtifactoryClient implements Closeable {
    private Logger logger = Logger.getLogger(getClass());

    // TODO: Needs a better name
    private BuildMap buildMap = new BuildMap();

    private ArtifactoryBuildInfoClient buildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.buildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, logger);
    }

    // TODO: is this really required?
    ArtifactoryClient(ArtifactoryBuildInfoClient buildInfoClient) {
        this.buildInfoClient = buildInfoClient;
    }

    public void uploadArtifacts(Collection<GoArtifact> artifacts) {
        for (GoArtifact artifact : artifacts) {
            upload(artifact);
        }
    }

    public void uploadBuildDetails(GoBuildDetails details) {
        Build build = buildMap.apply(details);
        try {
            buildInfoClient.sendBuildInfo(build);
        } catch (IOException e) {
            throw new RuntimeException("Unable to upload build info", e);
        }
    }

    @Override
    public void close() {
        buildInfoClient.shutdown();
    }

    private void upload(GoArtifact artifact) {
        File artifactFile = new File(artifact.localPath());

        try {
            Map<String, String> checksums = FileChecksumCalculator.calculateChecksums(artifactFile, "SHA1", "MD5");

            DeployDetails deployDetails = new DeployDetails.Builder()
                    .targetRepository(artifact.repository())
                    .artifactPath(artifact.artifactPath())
                    .file(artifactFile)
                    .sha1(checksums.get("SHA1"))
                    .md5(checksums.get("MD5"))
                    .addProperties(artifact.properties())
                    .build();

            buildInfoClient.deployArtifact(deployDetails);
        }
        catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to upload artifact: " + artifact, e);
        }
    }
}
