package com.tw.go.plugins.artifactory;

import com.tw.go.plugins.artifactory.model.GoBuildDetails;
import org.jfrog.build.api.Module;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class GoBuildDetailsBuilder {
    private String buildName;
    private String buildNumber;
    private DateTime startedAt;

    public GoBuildDetails build() {
        return new GoBuildDetails(buildName, buildNumber, startedAt);
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
}
