package com.tw.go.plugins.artifactory.task.config;

import org.junit.Test;

import static org.truth0.Truth.ASSERT;

public class UriConfigTest {
    @Test
    public void shouldRemoveTrailingSlashesFromUri() {
        UriConfig config = new UriConfig("ending/in/slash/", false);
        ASSERT.that(config.uri()).is("ending/in/slash");
    }
}