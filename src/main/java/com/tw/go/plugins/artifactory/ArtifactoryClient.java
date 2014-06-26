package com.tw.go.plugins.artifactory;

import com.google.common.base.Splitter;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ArtifactoryClient implements Closeable {
    private Logger logger = Logger.getLogger(getClass());

    private ArtifactoryBuildInfoClient buildInfoClient;

    public ArtifactoryClient(String artifactoryUrl, String user, String password) {
        this.buildInfoClient = new ArtifactoryBuildInfoClient(artifactoryUrl, user, password, logger);
    }

    public ArtifactoryClient(ArtifactoryBuildInfoClient buildInfoClient) {
        this.buildInfoClient = buildInfoClient;
    }

    public void uploadArtifact(String sourcePath, String destinationUri, Map<String, String> properties) throws IOException {
        List<String> uriSegments = Splitter.on("/").limit(2).splitToList(destinationUri);

        DeployDetails deployDetails = new DeployDetails.Builder()
                .targetRepository(uriSegments.get(0))
                .artifactPath(uriSegments.get(1))
                .file(new File(sourcePath))
                .addProperties(properties)
                .build();

        buildInfoClient.deployArtifact(deployDetails);
    }

    public void uploadBuildDetails(BuildDetails details) throws IOException {
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
