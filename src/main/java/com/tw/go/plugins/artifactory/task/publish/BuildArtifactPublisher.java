package com.tw.go.plugins.artifactory.task.publish;

import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.PluginProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

public class BuildArtifactPublisher {
    private PluginProperties plugin = new PluginProperties();

    public void publish(TaskExecutionContext context, Publishable publishable) {
        try {
            File publishableFile = new File(parent(context), publishable.name());

            OutputStream stream = new FileOutputStream(publishableFile);
            IOUtils.write(publishable.content(), stream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Error creating build artifact", e);
        }
    }

    private File parent(TaskExecutionContext context) throws IOException {
        File parent = Paths.get(context.workingDir(), plugin.name()).toFile();
        FileUtils.forceMkdir(parent);
        return parent;
    }
}
