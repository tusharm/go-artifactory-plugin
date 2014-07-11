package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.joda.time.DateTime;

import java.util.List;

import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.GO_PIPELINE_COUNTER;
import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.GO_PIPELINE_NAME;
import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.GO_STAGE_COUNTER;
import static java.lang.String.format;

public class GoBuildDetailsFactory {
    public GoBuildDetails createBuildDetails(EnvironmentVariables environment, List<GoArtifact> buildArtifacts) {
        String pipeline = GO_PIPELINE_NAME.from(environment);
        String pipelineCounter = GO_PIPELINE_COUNTER.from(environment);
        String stageCounter = GO_STAGE_COUNTER.from(environment);

        String buildNumber = format("%s.%s", pipelineCounter, stageCounter);

        GoBuildDetails buildDetails = new GoBuildDetails(pipeline, buildNumber, new DateTime());
        buildDetails.artifacts(buildArtifacts);

        return buildDetails;
    }
}
