package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import com.tw.go.plugins.artifactory.task.executor.PublishTaskExecutor;

import java.util.EnumSet;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;

@Extension
public class PublishTask extends GenericTask {

    public PublishTask() {
        this(EnumSet.of(uri, path));
    }

    public PublishTask(EnumSet<ConfigElement> configs) {
        super(configs);
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor(uri.name(), path.name());
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
