package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

public class UriConfigElement extends ConfigElement<UriConfig> {
    private static final String URI = "uri";

    protected UriConfigElement() {
        super(URI);
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
        return new UriConfig(taskConfig.getValue(URI));
    }
}
