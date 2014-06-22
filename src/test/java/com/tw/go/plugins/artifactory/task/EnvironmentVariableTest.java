package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.junit.Test;

import java.util.HashMap;

import static com.tw.go.plugins.artifactory.task.EnvironmentVariable.ARTIFACTORY_URL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.truth0.Truth.ASSERT;

public class EnvironmentVariableTest {
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

    private EnvironmentVariables asEnvVars(HashMap<String, String> envVars) {
        EnvironmentVariables environmentVariables = mock(EnvironmentVariables.class);
        when(environmentVariables.asMap()).thenReturn(envVars);
        return environmentVariables;
    }
}