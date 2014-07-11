package com.tw.go.plugins.artifactory.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.ImmutableList.copyOf;

public class GoBuildDetails {
    private String buildName;
    private String buildNumber;
    private DateTime startedAt;
    private List<GoArtifact> goArtifacts = new ArrayList<>();
    private Properties properties;

    public GoBuildDetails(String buildName, String buildNumber, DateTime startedAt) {
        this.buildName = buildName;
        this.buildNumber = buildNumber;
        this.startedAt = startedAt;
    }

    public String buildName() {
        return buildName;
    }

    public String buildNumber() {
        return buildNumber;
    }

    public DateTime startedAt() {
        return startedAt;
    }

    public void artifacts(List<GoArtifact> artifacts) {
        goArtifacts = copyOf(artifacts);
    }

    public List<GoArtifact> artifacts() {
        return copyOf(goArtifacts);
    }

    public Properties properties() {
        return properties;
    }

    public void properties(Properties properties) {
        this.properties = properties;
    }
}
