package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;

import java.io.File;

public class GoArtifactFactory {
    public GoArtifact createArtifact(TaskConfig config, TaskExecutionContext context) {
        String workingDir = context.workingDir();

        String path = ConfigElement.path.from(config);
        String uri = ConfigElement.uri.from(config);

        return new GoArtifact(workingDir + File.separator + path, uri);
    }
}
