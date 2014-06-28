package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.Iterables.skip;

public class GoArtifact {
    private String repository;
    private String localPath;
    private String artifactPath;
    private Map<String, String> properties;

    public GoArtifact(String localPath, String uri) {
        this.localPath = localPath;

        List<String> segments = Splitter.on("/").omitEmptyStrings().splitToList(uri);
        this.repository = segments.get(0);
        this.artifactPath = Joiner.on("/").join(skip(segments, 1));
    }

    public String localPath() {
        return localPath;
    }

    public String repository() {
        return repository;
    }

    public String artifactPath() {
        return artifactPath;
    }

    public void attachProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> properties() {
        return copyOf(properties);
    }
}
