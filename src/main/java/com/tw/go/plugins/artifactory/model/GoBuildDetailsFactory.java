package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.*;
import static java.lang.String.format;

public class GoBuildDetailsFactory {
    public GoBuildDetails createBuildDetails(Map<String, String> buildProperties, EnvironmentVariables environment, Collection<GoArtifact> buildArtifacts) {
        String pipeline = GO_PIPELINE_NAME.from(environment);
        String pipelineCounter = GO_PIPELINE_COUNTER.from(environment);
        String stageCounter = GO_STAGE_COUNTER.from(environment);

        String buildNumber = format("%s.%s", pipelineCounter, stageCounter);

        GoBuildDetails buildDetails = new GoBuildDetails(pipeline, buildNumber, DateTime.now());
        buildDetails.artifacts(buildArtifacts);

        Properties properties = new Properties();
        properties.putAll(buildProperties);
        buildDetails.properties(properties);

        return buildDetails;
    }
}
