package com.tw.go.plugins.artifactory.model;

import org.joda.time.DateTime;

import java.util.*;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableMap.copyOf;

public class GoBuildDetails {
    private String name;
    private String number;
    private String url;
    private DateTime startedAt;
    private Collection<GoArtifact> goArtifacts = new ArrayList<>();
    private Map<String, String> envVars = new HashMap<>();

    public GoBuildDetails(String name, String number, DateTime startedAt) {
        this.name = name;
        this.number = number;
        this.startedAt = startedAt;
    }

    public String buildName() {
        return name;
    }

    public String buildNumber() {
        return number;
    }

    public DateTime startedAt() {
        return startedAt;
    }

    public void artifacts(Collection<GoArtifact> artifacts) {
        goArtifacts = artifacts;
    }

    public List<GoArtifact> artifacts() {
        return copyOf(goArtifacts);
    }

    public String url() {
        return url;
    }

    public void url(String url) {
        this.url = url;
    }

    public void environmentVariables(Map<String, String> envVars) {
        this.envVars = envVars;
    }

    public Map<String, String> environmentVariables() {
        return copyOf(this.envVars);
    }
}
