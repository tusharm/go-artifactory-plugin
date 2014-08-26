package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.UriConfig;
import com.tw.go.plugins.artifactory.utils.filesystem.DirectoryScanner;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Collections2.transform;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uriConfig;

public class GoArtifactFactory {

    public Collection<GoArtifact> createArtifacts(final TaskConfig config, TaskExecutionContext context) {
        DirectoryScanner scanner = new DirectoryScanner(context.workingDir());
        Collection<File> files = scanner.scan(path.from(config));

        return transform(files, goArtifact(uriConfig.from(config), buildProperties.from(config)));
    }

    private Function<File, GoArtifact> goArtifact(final UriConfig config, final Map<String, String> properties) {
        return new Function<File, GoArtifact>() {
            @Override
            public GoArtifact apply(File file) {
                GoArtifact artifact = new GoArtifact(file.getAbsolutePath(), artifactUri(file.getName()));
                artifact.properties(properties);
                return artifact;
            }

            private String artifactUri(String artifactName) {
                return config.isFolder() ? config.uri() + "/" + artifactName : config.uri();
            }
        };
    }
}
