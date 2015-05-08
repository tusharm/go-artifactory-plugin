package com.tw.go.plugins.artifactory.client;

import com.google.common.base.Function;
import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Artifact;
import org.jfrog.build.api.Build;
import org.jfrog.build.api.Module;
import org.jfrog.build.api.builder.ArtifactBuilder;
import org.jfrog.build.api.builder.BuildInfoBuilder;
import org.jfrog.build.api.builder.ModuleBuilder;

import java.util.*;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.GO_PIPELINE_COUNTER;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.GO_STAGE_COUNTER;
import static java.lang.String.format;
import static org.jfrog.build.api.Build.STARTED_FORMAT;
import static org.jfrog.build.api.BuildInfoProperties.BUILD_INFO_ENVIRONMENT_PREFIX;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class BuildMap implements Function<GoBuildDetails, Build> {

    @Override
    public Build apply(GoBuildDetails buildDetails) {
        // Changed the id format for the usual default, with build info
        Module module = new ModuleBuilder()
                .id(format("%s:%s", buildDetails.buildName(),buildDetails.buildNumber()))
                .artifacts(artifactsFrom(buildDetails))
                .build();

        return new BuildInfoBuilder(buildDetails.buildName())
                .url(buildDetails.url())
                .number(buildDetails.buildNumber())
                .started(forPattern(STARTED_FORMAT).print(buildDetails.startedAt()))
                .addModule(module)
                .properties(environmentProperties(buildDetails))
                .build();
    }

    private List<Artifact> artifactsFrom(GoBuildDetails buildDetails) {
        return newArrayList(transform(buildDetails.artifacts(), new Function<GoArtifact, Artifact>() {
            @Override
            public Artifact apply(GoArtifact artifact) {
                return new ArtifactBuilder(artifact.remoteName())
                        .md5(artifact.md5())
                        .sha1(artifact.sha1())
                        .build();
            }
        }));
    }

    private Properties environmentProperties(GoBuildDetails buildDetails) {
        Map<String, String> map = new HashMap<>();
        for (String envVar : buildDetails.environmentVariables().keySet()) {
            map.put(BUILD_INFO_ENVIRONMENT_PREFIX + envVar, buildDetails.environmentVariables().get(envVar));
        }

        Properties properties = new Properties();
        properties.putAll(map);
        return properties;
    }

}
