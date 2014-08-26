package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskConfigProperty;
import com.tw.go.plugins.artifactory.Logger;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.lang.Boolean.valueOf;

public class UriConfigElement extends ConfigElement<UriConfig> {
    private static final String URI = "URI";
    public static final String IS_FOLDER = "uriIsFolder";

    protected UriConfigElement() {
        super(URI, IS_FOLDER);
    }

    @Override
    public Optional<ValidationError> validate(TaskConfig taskConfig) {
        String uri = from(taskConfig).uri();

        if (uri.isEmpty())
            return of(new ValidationError(URI, "Uri is mandatory"));

        if (!uri.matches("[^/].*"))
            return of(new ValidationError(URI, "Relative uri should not start with a '/'"));

        return absent();
    }

    @Override
    public UriConfig from(TaskConfig taskConfig) {
        String uri = taskConfig.getValue(URI);
        String isFolder = taskConfig.getValue(IS_FOLDER);
        return new UriConfig(uri, valueOf(isFolder));
    }

    @Override
    public void addTo(TaskConfig taskConfig) {
        taskConfig.add(new TaskConfigProperty(URI, ""));
        taskConfig.add(new TaskConfigProperty(IS_FOLDER, "false"));
    }
}
