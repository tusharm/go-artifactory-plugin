package com.tw.go.plugins.artifactory.client;

import com.google.common.base.Function;
import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Artifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.Module;
import org.jfrog.build.api.builder.ArtifactBuilder;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.api.builder.ModuleBuilder;
import org.jfrog.build.api.util.FileChecksumCalculator;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.google.common.collect.Collections2.transform;
import static org.jfrog.build.api.Build.STARTED_FORMAT;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class ArtifactoryClient implements Closeable {
    private Logger logger = Logger.getLogger(getClass());

    private ArtifactoryBuildInfoClient buildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.buildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, logger);
    }

    ArtifactoryClient(ArtifactoryBuildInfoClient buildInfoClient) {
        this.buildInfoClient = buildInfoClient;
    }

    public void uploadArtifacts(Collection<GoArtifact> artifacts) throws IOException, NoSuchAlgorithmException {
        for (GoArtifact artifact : artifacts) {
            uploadArtifact(artifact);
        }
    }

    public void uploadBuildDetails(GoBuildDetails details) throws IOException {
        Collection<Artifact> artifacts = mapFrom(details.artifacts());

        Module module = new ModuleBuilder().id(details.buildName())
                .artifacts(new ArrayList(artifacts))
                .build();

        Build build = new BuildInfoBuilder(details.buildName())
                .url(details.url())
                .number(details.buildNumber())
                .started(forPattern(STARTED_FORMAT).print(details.startedAt()))
                .addModule(module)
                .build();

        buildInfoClient.sendBuildInfo(build);
    }

    @Override
    public void close() throws IOException {
        buildInfoClient.shutdown();
    }

    private void uploadArtifact(GoArtifact artifact) throws IOException, NoSuchAlgorithmException {
        File artifactFile = new File(artifact.localPath());

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

    private Collection<Artifact> mapFrom(List<GoArtifact> goArtifacts) {
        return transform(goArtifacts, new Function<GoArtifact, Artifact>() {
            @Override
            public Artifact apply(GoArtifact goArtifact) {
                return new ArtifactBuilder(goArtifact.localPath()).build();
            }
        });
    }
}
