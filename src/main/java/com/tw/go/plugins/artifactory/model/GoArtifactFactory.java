package com.tw.go.plugins.artifactory.model;

import static com.google.common.collect.Collections2.transform;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uriConfig;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import com.google.common.base.Function;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.UriConfig;
import com.tw.go.plugins.artifactory.utils.filesystem.DirectoryScanner;

public class GoArtifactFactory {

    public Collection<GoArtifact> createArtifacts(final TaskConfig config, TaskExecutionContext context) {
        DirectoryScanner scanner = new DirectoryScanner(context.workingDir());
        Collection<File> files = scanner.scan(path.from(config));        

        return transform(files, goArtifact(uriConfig.from(config), buildProperties.from(config), context));
    }

    private Function<File, GoArtifact> goArtifact(final UriConfig config, final Map<String, String> properties, final TaskExecutionContext context) {
        return new Function<File, GoArtifact>() {
            @Override
            public GoArtifact apply(File file) {
                GoArtifact artifact = new GoArtifact(file.getAbsolutePath(), artifactUri(file.getName()));
                artifact.properties(properties);
                return artifact;
            }

            private String artifactUri(String artifactName) {
                String uri = config.isFolder() ? config.uri() + "/" + artifactName : config.uri();
                StrSubstitutor sub = new StrSubstitutor(context.environment().asMap(), "${", "}");
                return sub.replace(uri);
            }
            
        };
    }
}
