package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.Console;
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
        put("ARTIFACTORY_PASSWORD", "passwd");
    }};

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

        ASSERT.that(details.artifacts()).has().exactly(artifact);
        ASSERT.that(details.buildName()).is("pipeline");
        ASSERT.that(details.buildNumber()).is("pipelineCounter.stageCounter");
        ASSERT.that(details.url()).is("https://localhost:8154/go/pipelines/value_stream_map/pipeline/pipelineCounter");

        ASSERT.that(details.environmentVariables()).isEqualTo(envVars);
    }

    @Test
    public void shouldObfuscateSecureVariables() {
        when(secureEnvSpecifier.isSecure("ARTIFACTORY_PASSWORD")).thenReturn(true);

        GoArtifact artifact = new GoArtifact("path", "uri/path");
        GoBuildDetails details = factory.createBuildDetails(environment, asList(artifact));

        ASSERT.that(details.environmentVariables()).hasKey("ARTIFACTORY_PASSWORD").withValue("****");
    }
}