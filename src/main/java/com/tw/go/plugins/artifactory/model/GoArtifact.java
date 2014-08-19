package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import org.jfrog.build.api.util.FileChecksumCalculator;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.getLast;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;

public class GoArtifact {
    private String repository;
    private String localPath;
    private String remotePath;
    private Splitter splitter;
    private Map<String, String> properties = new HashMap<>();
    private Map<String, String> checksums = emptyMap();


    public GoArtifact(String localPath, String uri) {
        this.localPath = localPath;
        this.splitter = Splitter.on("/").omitEmptyStrings().trimResults();

        List<String> segments = splitter.limit(2).splitToList(uri);
        this.repository = segments.get(0);
        this.remotePath = segments.get(1);
    }

    public String localPath() {
        return localPath;
    }

    public String repository() {
        return repository;
    }

    public String remotePath() {
        return remotePath;
    }

    public String remoteName() {
        return getLast(splitter.split(remotePath));
    }

    public void properties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> properties() {
        return properties;
    }

    public String sha1() {
        if (checksums.isEmpty())
            this.checksums = computeChecksums();

        return checksums.get("SHA1");
    }

    public String md5() {
        if (checksums.isEmpty())
            this.checksums = computeChecksums();

        return checksums.get("MD5");
    }

    @Override
    public int hashCode() {
        int result = repository.hashCode();
        result = 31 * result + localPath.hashCode();
        result = 31 * result + remotePath.hashCode();
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GoArtifact)) return false;

        GoArtifact artifact = (GoArtifact) o;

        return remotePath.equals(artifact.remotePath)
                && localPath.equals(artifact.localPath)
                && repository.equals(artifact.repository)
                && properties.equals(artifact.properties);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("path", localPath)
                .add("uri", format("%s/%s", repository, remotePath))
                .add("properties", properties)
                .toString();
    }

    private Map<String, String> computeChecksums() {
        try {
            return FileChecksumCalculator.calculateChecksums(new File(localPath), "SHA1", "MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
