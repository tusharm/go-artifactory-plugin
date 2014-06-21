package com.tw.go.plugins.artifactory.task.view;

import com.google.common.io.CharStreams;
import com.thoughtworks.go.plugin.api.task.TaskView;
import com.tw.go.plugins.artifactory.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.String.format;

public class TemplateBasedTaskView implements TaskView {
    private final Logger logger = Logger.getLogger(this.getClass());

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
            String message = format("Could not load view template [%s]!", template);
            logger.error(message, e);
            return message;
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
}
