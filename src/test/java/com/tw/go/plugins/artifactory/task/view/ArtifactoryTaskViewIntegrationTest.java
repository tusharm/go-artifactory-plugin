package com.tw.go.plugins.artifactory.task.view;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ArtifactoryTaskViewIntegrationTest {
    @Test
    @Ignore("the call to Logger in PublishTaskView results in an error")
    public void shouldReadViewTemplateFromClasspath() {
        ArtifactoryTaskView view = new ArtifactoryTaskView("displayValue", "test");

        assertThat(view.displayValue(), is("displayValue"));
        assertThat(view.template(), is("<html></html>"));
    }
}