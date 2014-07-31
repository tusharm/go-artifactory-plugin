package com.tw.go.plugins.artifactory.task.executor;

import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskExecutionContextBuilder {
    private HashMap<String, String> envVars = new HashMap<>();
    private String workingDir;

    public TaskExecutionContextBuilder withEnvVar(String name, String value) {
        envVars.put(name, value);
        return this;
    }

    public TaskExecutionContextBuilder withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    public TaskExecutionContext build() {
        EnvironmentVariables environmentVariables = mock(EnvironmentVariables.class);
        when(environmentVariables.asMap()).thenReturn(envVars);

        TaskExecutionContext context = mock(TaskExecutionContext.class);
        when(context.environment()).thenReturn(environmentVariables);
        when(context.workingDir()).thenReturn(workingDir);

        Console console = mock(Console.class);
        when(context.console()).thenReturn(console);

        return context;
    }
}
