package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;

@Extension
public class ArtifactoryTask implements Task {
  public TaskConfig config() {
    return null;
  }

  public ValidationResult validate(TaskConfig config) {
    return null;
  }

  public TaskView view() {
    return null;
  }

  public TaskExecutor executor() {
    return null;
  }
}
