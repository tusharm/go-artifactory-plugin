package com.tw.go.plugins.artifactory;

import org.joda.time.DateTime;

public class BuildDetailsBuilder {
    private String buildName;
    private String buildNumber;
    private DateTime startedAt;

    public BuildDetails build() {
        return new BuildDetails(buildName, buildNumber, startedAt);
    }

    public BuildDetailsBuilder buildName(String name) {
        this.buildName = name;
        return this;
    }

    public BuildDetailsBuilder buildNumber(String number) {
        buildNumber = number;
        return this;
    }

    public BuildDetailsBuilder startedAt(DateTime startedAt) {
        this.startedAt = startedAt;
        return this;
    }
}
