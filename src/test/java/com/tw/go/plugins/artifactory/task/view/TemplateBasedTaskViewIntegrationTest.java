package com.tw.go.plugins.artifactory.task.view;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TemplateBasedTaskViewIntegrationTest {
    @Test
    public void shouldReadViewTemplateFromClasspath() {
        TemplateBasedTaskView view = new TemplateBasedTaskView("displayValue", "test");

        assertThat(view.displayValue(), is("displayValue"));
        assertThat(view.template(), is("<html></html>"));
    }
}