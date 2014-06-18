package com.tw.go.plugins.artifactory.task.view;

import com.google.common.io.CharStreams;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.task.TaskView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.thoughtworks.go.plugin.api.logging.Logger.getLoggerFor;
import static java.lang.String.format;

public class TemplateBasedTaskView implements TaskView {
    private final Logger logger = getLoggerFor(this.getClass());

    private final String title;
    private final String template;

    public TemplateBasedTaskView(String title, String template) {
        this.title = title;
        this.template = template;
    }

    @Override
    public String displayValue() {
        return title;
    }

    @Override
    public String template() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath());
            return read(inputStream);
        } catch (IOException e) {
            return log(format("Could not load view template [%s]!", template), e);
        }
    }

    private String templatePath() {
        return format("view/%s.html", template);
    }

    private String read(InputStream stream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
            return CharStreams.toString(reader);
        }
    }

    private String log(String contextualizedMessage, Exception ex) {
        logger.error(format("%s: %s", contextualizedMessage, ex.getMessage()));
        return contextualizedMessage;
    }
}
