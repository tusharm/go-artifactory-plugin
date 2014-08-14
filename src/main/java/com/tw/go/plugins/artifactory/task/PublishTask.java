package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.model.GoArtifactFactory;
import com.tw.go.plugins.artifactory.model.GoBuildDetailsFactory;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import com.tw.go.plugins.artifactory.task.executor.PublishTaskExecutor;

import java.util.List;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.*;
import static java.util.Arrays.asList;

@Extension
public class PublishTask extends GenericTask {
    private GoArtifactFactory artifactFactory = new GoArtifactFactory();
    private GoBuildDetailsFactory buildDetailsFactory = new GoBuildDetailsFactory();

    public PublishTask() {
        this(asList(uri, path, buildProperties));
    }

    public PublishTask(List<ConfigElement<?>> configs) {
        super(configs);
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor(artifactFactory, buildDetailsFactory);
    }

    @Override
    protected String taskViewName() {
        return "publish";
    }

    @Override
    protected String taskDisplayName() {
        return "Publish to Artifactory";
    }
}
