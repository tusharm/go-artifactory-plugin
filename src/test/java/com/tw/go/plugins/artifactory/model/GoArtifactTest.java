package com.tw.go.plugins.artifactory.model;

import org.junit.Test;

import static org.truth0.Truth.ASSERT;

public class GoArtifactTest {
    @Test
    public void shouldSplitUriIntoRepoAndArtifactPath() {
        GoArtifact artifact = new GoArtifact("dont/care", "repo/path/to/artifact.ext");
        ASSERT.that(artifact.repository()).is("repo");
        ASSERT.that(artifact.artifactPath()).is("path/to/artifact.ext");
    }
}