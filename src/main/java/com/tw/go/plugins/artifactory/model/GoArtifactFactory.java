package com.tw.go.plugins.artifactory.model;

import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.utils.DirectoryScanner;

import java.io.File;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uri;

public class GoArtifactFactory {

    public Collection<GoArtifact> createArtifacts(final TaskConfig config, TaskExecutionContext context) {
        DirectoryScanner scanner = new DirectoryScanner(context.workingDir());
        Collection<File> files = scanner.scan(path.from(config));

        boolean multipleFiles = files.size() > 1;
        return transform(files, new GoArtifactMapper(uri.from(config), multipleFiles));
    }

}
