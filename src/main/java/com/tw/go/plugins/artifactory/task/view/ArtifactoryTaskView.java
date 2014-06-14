package com.tw.go.plugins.artifactory.task.view;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.task.TaskView;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor;
import static java.lang.String.format;

public class ArtifactoryTaskView implements TaskView {
    private final Logger logger = getLoggerFor(this.getClass());

    private final String template;
    private final String displayValue;

    public ArtifactoryTaskView(String template, String displayValue) {
        this.template = template;
        this.displayValue = displayValue;
    }

    @Override
    public String displayValue() {
        return displayValue;
    }

    @Override
    public String template() {
        try {
            return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(templatePath()));
        } catch (IOException e) {
            String message = format("Could not load view template [%s]!", template);
            logger.error(format("%s: %s", message, e.getMessage()));
            return message;
        }
    }

    private String templatePath() {
        return format("view/%s.html", template);
    }
}
