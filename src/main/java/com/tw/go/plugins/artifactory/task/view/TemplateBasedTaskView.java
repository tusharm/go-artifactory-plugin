package com.tw.go.plugins.artifactory.task.view;

import com.thoughtworks.go.plugin.api.task.TaskView;
import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.utils.filesystem.ClasspathResource;

import java.io.IOException;

import static java.lang.String.format;

public class TemplateBasedTaskView implements TaskView {
    private final Logger logger = Logger.getLogger(this.getClass());

    private final String title;
    private final String template;
    private final ClasspathResource classpathResource;

    public TemplateBasedTaskView(String title, String template) {
        this.title = title;
        this.template = template;
        this.classpathResource = new ClasspathResource();
    }

    @Override
    public String displayValue() {
        return title;
    }

    @Override
    public String template() {
        try {
            return classpathResource.read(templatePath());
        } catch (IOException e) {
            String message = format("Could not load view template [%s]!", template);
            logger.error(message, e);
            return message;
        }
    }

    private String templatePath() {
        return format("view/%s.html", template);
    }
}
