package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.tw.go.plugins.artifactory.GoBuildDetailsBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.tw.go.plugins.artifactory.testutils.MapBuilder.map;
import static com.tw.go.plugins.artifactory.testutils.matchers.DeepEqualsMatcher.deepEquals;
import static com.tw.go.plugins.artifactory.testutils.matchers.DeepEqualsMatcher.ignoring;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class GoBuildDetailsFactoryTest {
    private Map<String, String> envVars = map("GO_PIPELINE_NAME", "pipeline")
            .and("GO_PIPELINE_COUNTER", "pipelineCounter")
            .and("GO_STAGE_COUNTER", "stageCounter")
            .and("GO_SERVER_URL", "https://localhost:8154/go/")
            .and("ARTIFACTORY_PASSWORD", "passwd");

    private EnvironmentVariables environment;
    private Console.SecureEnvVarSpecifier secureEnvSpecifier;
    private GoBuildDetailsFactory factory;

    @Before
    public void beforeEach() {
        secureEnvSpecifier = mock(Console.SecureEnvVarSpecifier.class);

        environment = mock(EnvironmentVariables.class);
        when(environment.asMap()).thenReturn(envVars);
        when(environment.secureEnvSpecifier()).thenReturn(secureEnvSpecifier);

        factory = new GoBuildDetailsFactory();
    }

    @Test
    public void shouldCreateBuildDetails() {
        GoArtifact artifact = new GoArtifact("path", "uri/path");
        GoBuildDetails details = factory.createBuildDetails(environment, asList(artifact));

        GoBuildDetails expected = new GoBuildDetailsBuilder()
                .buildName("pipeline")
                .buildNumber("pipelineCounter.stageCounter")
                .url("https://localhost:8154/go/pipelines/value_stream_map/pipeline/pipelineCounter")
                .artifact(artifact)
                .envVariables(envVars)
                .build();

        assertThat(details, deepEquals(expected, ignoring("startedAt")));
    }

    @Test
    public void shouldObfuscateSecureVariables() {
        when(secureEnvSpecifier.isSecure("ARTIFACTORY_PASSWORD")).thenReturn(true);

        GoArtifact artifact = new GoArtifact("path", "uri/path");
        GoBuildDetails details = factory.createBuildDetails(environment, asList(artifact));

        ASSERT.that(details.environmentVariables()).hasKey("ARTIFACTORY_PASSWORD").withValue("****");
    }
}