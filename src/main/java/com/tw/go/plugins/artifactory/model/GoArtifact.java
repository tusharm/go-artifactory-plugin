package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;

import java.util.List;

import static java.lang.String.format;

public class GoArtifact {
    private String repository;
    private String localPath;
    private String artifactPath;

    public GoArtifact(String localPath, String uri) {
        this.localPath = localPath;

        List<String> segments = Splitter.on("/").limit(2).omitEmptyStrings().splitToList(uri);
        this.repository = segments.get(0);
        this.artifactPath = segments.get(1);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GoArtifact)) return false;

        GoArtifact artifact = (GoArtifact) o;

        return artifactPath.equals(artifact.artifactPath)
                && localPath.equals(artifact.localPath)
                && repository.equals(artifact.repository);
    }

    @Override
    public int hashCode() {
        int result = repository.hashCode();
        result = 31 * result + localPath.hashCode();
        result = 31 * result + artifactPath.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("path", localPath)
                .add("uri", format("%s/%s", repository, artifactPath))
                .toString();
    }
}
