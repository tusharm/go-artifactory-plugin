package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class GoBuildDetailsFactoryTest {
    private Map<String, String> envVars = new HashMap() {{
        put("GO_PIPELINE_NAME", "pipeline");
        put("GO_PIPELINE_COUNTER", "pipelineCounter");
        put("GO_STAGE_COUNTER", "stageCounter");
        put("GO_SERVER_URL", "https://localhost:8154/go/");
    }};

    private EnvironmentVariables environment;

    @Before
    public void beforeEach() {
        environment = mock(EnvironmentVariables.class);
        when(environment.asMap()).thenReturn(envVars);
    }

    @Test
    public void shouldCreateBuildDetails() {
        GoBuildDetailsFactory factory = new GoBuildDetailsFactory();

        GoArtifact artifact = new GoArtifact("path", "uri/path");
        GoBuildDetails details = factory.createBuildDetails(environment, asList(artifact));

        ASSERT.that(details.artifacts()).has().exactly(artifact);
        ASSERT.that(details.buildName()).is("pipeline");
        ASSERT.that(details.buildNumber()).is("pipelineCounter.stageCounter");
        ASSERT.that(details.url()).is("https://localhost:8154/go/pipelines/value_stream_map/pipeline/pipelineCounter");

        ASSERT.that(details.environmentVariables()).isEqualTo(envVars);
    }
}