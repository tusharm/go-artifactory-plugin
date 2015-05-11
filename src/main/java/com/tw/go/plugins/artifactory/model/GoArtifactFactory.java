package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;
import static com.google.common.collect.Collections2.transform;
import com.thoughtworks.go.plugin.api.config.Property;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.GO_PIPELINE_COUNTER;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.GO_PIPELINE_NAME;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.GO_STAGE_COUNTER;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uriConfig;
import com.tw.go.plugins.artifactory.task.config.UriConfig;
import com.tw.go.plugins.artifactory.utils.filesystem.DirectoryScanner;
import java.io.File;
import static java.lang.String.format;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GoArtifactFactory {

    public Collection<GoArtifact> createArtifacts(final TaskConfig config, TaskExecutionContext context) {
        DirectoryScanner scanner = new DirectoryScanner(context.workingDir());
        Collection<File> files = scanner.scan(path.from(config));
        
        //Added default build properties to make Artifactory-Pro promotions work
        Map<String, String> properties = new HashMap<>(buildProperties.from(config));
        properties.put("build.name", GO_PIPELINE_NAME.from(context.environment()));
        properties.put("build.number", buildNumber(context.environment()));
        
        return transform(files, goArtifact(uriConfig.from(config), properties));
    }
    private String buildNumber(EnvironmentVariables envVars) {
        return format("%s.%s", GO_PIPELINE_COUNTER.from(envVars), GO_STAGE_COUNTER.from(envVars));
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
