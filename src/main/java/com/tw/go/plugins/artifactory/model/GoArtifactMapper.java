package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;

import java.io.File;
import java.util.Map;

import static java.lang.String.format;

class GoArtifactMapper implements Function<File, GoArtifact> {
    private static final String EMPTYSTRING = "";
    private boolean multipleFiles;
    private String uri;
    private Map<String, String> properties;

    public GoArtifactMapper(String uri, Map<String, String> properties, boolean multipleFiles) {
        this.uri = uri;
        this.properties = properties;
        this.multipleFiles = multipleFiles;
    }

    @Override
    public GoArtifact apply(File file) {
        GoArtifact artifact = new GoArtifact(file.getAbsolutePath(), artifactUri(file));
        artifact.properties(properties);
        return artifact;
    }

    private String artifactUri(File file) {
        String suffix = multipleFiles ? format("/%s", file.getName()) : EMPTYSTRING;
        return format("%s%s", uri, suffix);
    }
}
