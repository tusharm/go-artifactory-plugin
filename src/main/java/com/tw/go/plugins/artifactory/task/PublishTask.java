package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.task.config.ConfigElement;
import com.tw.go.plugins.artifactory.task.executor.PublishTaskExecutor;

import java.util.EnumSet;
import java.util.List;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.properties;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;
import static java.util.Arrays.asList;

@Extension
public class PublishTask extends GenericTask {

    public PublishTask() {
        this(asList(uri, path, properties));
    }

    public PublishTask(List<ConfigElement<?>> configs) {
        super(configs);
    }

    @Override
    public TaskExecutor executor() {
        return new PublishTaskExecutor();
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
