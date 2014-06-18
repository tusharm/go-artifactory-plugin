package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import org.junit.Test;

import static com.tw.go.plugins.artifactory.task.config.ConfigElement.url;
import static org.truth0.Truth.ASSERT;


public class UrlConfigElementTest {
    @Test
    public void shouldValidateAnHTTPUrl() {
        Optional<ValidationError> error = url.validate("http://localhost");
        ASSERT.that(error).isAbsent();
    }

    @Test
    public void shouldValidateAnHTTPSUrl() {
        Optional<ValidationError> error = url.validate("https://localhost:8081/artifactory");
        ASSERT.that(error).isAbsent();
    }

    @Test
    public void shouldNotValidateIfNotHTTPOrHTTPS() {
        Optional<ValidationError> error = url.validate("localhost:8081/artifactory");
        ASSERT.that(error).hasValue(new ValidationError(url.name(), "Invalid HTTP URL"));
    }
}