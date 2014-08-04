package com.tw.go.plugins.artifactory.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.ImmutableList.copyOf;

public class GoBuildDetails {
    private String name;
    private String number;
    private String url;
    private DateTime startedAt;
    private List<GoArtifact> goArtifacts = new ArrayList<>();

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
        goArtifacts = copyOf(artifacts);
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
}
