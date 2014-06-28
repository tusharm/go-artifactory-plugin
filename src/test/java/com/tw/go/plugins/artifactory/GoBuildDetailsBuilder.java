package com.tw.go.plugins.artifactory;

import com.tw.go.plugins.artifactory.model.GoArtifact;
import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.joda.time.DateTime;

import static java.util.Arrays.asList;

public class GoBuildDetailsBuilder {
    private String buildName;
    private String buildNumber;
    private DateTime startedAt;
    private GoArtifact artifact;

    public GoBuildDetails build() {
        GoBuildDetails buildDetails = new GoBuildDetails(buildName, buildNumber, startedAt);
        buildDetails.artifacts(asList(artifact));
        return buildDetails;
    }

    public GoBuildDetailsBuilder buildName(String name) {
        this.buildName = name;
        return this;
    }

    public GoBuildDetailsBuilder buildNumber(String number) {
        buildNumber = number;
        return this;
    }

    public GoBuildDetailsBuilder startedAt(DateTime startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public GoBuildDetailsBuilder artifact(GoArtifact artifact) {
        this.artifact = artifact;
        return this;
    }
}
