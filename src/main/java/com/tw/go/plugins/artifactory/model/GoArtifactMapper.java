package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;

import java.io.File;

import static java.lang.String.format;

class GoArtifactMapper implements Function<File, GoArtifact> {
    private static final String EMPTYSTRING = "";
    private boolean multipleFiles;
    private String uri;

    public GoArtifactMapper(String uri, boolean multipleFiles) {
        this.uri = uri;
        this.multipleFiles = multipleFiles;
    }

    @Override
    public GoArtifact apply(File file) {
        return new GoArtifact(file.getAbsolutePath(), artifactUri(file));
    }

    private String artifactUri(File file) {
        String suffix = multipleFiles ? format("/%s", file.getName()) : EMPTYSTRING;
        return format("%s%s", uri, suffix);
    }
}
