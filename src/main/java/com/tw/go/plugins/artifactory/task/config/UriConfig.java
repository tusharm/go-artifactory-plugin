package com.tw.go.plugins.artifactory.task.config;

import static org.apache.commons.lang.StringUtils.chop;

public class UriConfig {
    private String uri;
    private boolean folder;

    public UriConfig(String uri, boolean folder) {
        this.uri = uri.endsWith("/") ? chop(uri) : uri;
        this.folder = folder;
    }

    public String uri() {
        return uri;
    }

    public boolean isFolder() {
        return folder;
    }
}
