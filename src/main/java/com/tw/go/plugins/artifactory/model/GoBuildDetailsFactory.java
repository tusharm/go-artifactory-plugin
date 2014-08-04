package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Properties;

import static com.tw.go.plugins.artifactory.task.EnvironmentData.*;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static java.lang.String.format;

public class GoBuildDetailsFactory {

    public GoBuildDetails createBuildDetails(EnvironmentVariables envVars, Collection<GoArtifact> buildArtifacts) {
        GoBuildDetails buildDetails =
                new GoBuildDetails(GO_PIPELINE_NAME.from(envVars), buildNumber(envVars), DateTime.now());

        buildDetails.url(PIPELINE_VALUESTREAM_URL.from(envVars));
        buildDetails.artifacts(buildArtifacts);

        return buildDetails;
    }

    private String buildNumber(EnvironmentVariables envVars) {
        return format("%s.%s", GO_PIPELINE_COUNTER.from(envVars), GO_STAGE_COUNTER.from(envVars));
    }
}
