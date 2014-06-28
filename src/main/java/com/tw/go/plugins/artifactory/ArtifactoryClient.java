package com.tw.go.plugins.artifactory;

import com.google.common.base.Function;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import org.jfrog.build.api.Artifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.Module;
import org.jfrog.build.api.builder.ArtifactBuilder;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.api.builder.ModuleBuilder;
import org.jfrog.build.client.ArtifactoryBuildInfoClient;
import org.jfrog.build.client.DeployDetails;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.jfrog.build.api.Build.STARTED_FORMAT;
import static org.joda.time.format.DateTimeFormat.forPattern;

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
        List<Artifact> artifacts = mapFrom(details.artifacts());

        Module module = new ModuleBuilder().id(details.buildName())
                .artifacts(artifacts)
                .build();

        Build build = new BuildInfoBuilder(details.buildName())
                .number(details.buildNumber())
                .started(forPattern(STARTED_FORMAT).print(details.startedAt()))
                .addModule(module)
                .build();

        buildInfoClient.sendBuildInfo(build);
    }

    private List<Artifact> mapFrom(List<GoArtifact> goArtifacts) {
        return (List<Artifact>) com.tw.go.plugins.artifactory.utils.Iterables.map(goArtifacts, new Function<GoArtifact, Artifact>() {
            @Override
            public Artifact apply(GoArtifact goArtifact) {
                Properties properties = new Properties();
                properties.putAll(goArtifact.properties());

                return new ArtifactBuilder(goArtifact.localPath())
                        .properties(properties)
                        .build();
            }
        });
    }

    @Override
    public void close() throws IOException {
        buildInfoClient.shutdown();
    }
}
