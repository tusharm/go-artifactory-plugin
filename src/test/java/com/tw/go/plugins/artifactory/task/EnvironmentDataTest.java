package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.junit.Test;

import java.util.HashMap;

import static com.tw.go.plugins.artifactory.task.EnvironmentData.ARTIFACTORY_URL;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.PIPELINE_VALUESTREAM_URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class EnvironmentDataTest {
    @Test
    public void shouldReturnEnvironmentVariableValue() {
        EnvironmentVariables environmentVariables = asEnvVars(new HashMap<String, String>() {{
            put("ARTIFACTORY_URL", "http://localhost");
        }});

        ASSERT.that(ARTIFACTORY_URL.from(environmentVariables)).isEqualTo("http://localhost");
    }

    @Test(expected = EnvironmentVariableMissingException.class)
    public void shouldThrowExceptionIfEnvironmentVariableIsMissing() {
        EnvironmentVariables environmentVariables = asEnvVars(new HashMap<String, String>());
        ARTIFACTORY_URL.from(environmentVariables);
    }

    @Test
    public void shouldReturnValueStreamUrlForThePipeline() {
        EnvironmentVariables environmentVariables = asEnvVars(new HashMap<String, String>() {{
            put("GO_SERVER_URL", "http://go.server:8153/go/");
            put("GO_PIPELINE_NAME", "name");
            put("GO_PIPELINE_COUNTER", "23");
        }});

        ASSERT.that(PIPELINE_VALUESTREAM_URL.from(environmentVariables))
                .is("http://go.server:8153/go/pipelines/value_stream_map/name/23");

    }

    private EnvironmentVariables asEnvVars(HashMap<String, String> envVars) {
        EnvironmentVariables environmentVariables = mock(EnvironmentVariables.class);
        when(environmentVariables.asMap()).thenReturn(envVars);
        return environmentVariables;
    }
}