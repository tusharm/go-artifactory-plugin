package com.tw.go.plugins.artifactory.model;

import org.joda.time.DateTime;

public class GoBuildDetails {
    private String buildName;
    private String buildNumber;
    private DateTime startedAt;

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
}
