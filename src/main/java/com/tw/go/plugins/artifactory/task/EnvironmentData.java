package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;

import java.util.Map;

import static java.lang.String.format;

public enum EnvironmentData {
    ARTIFACTORY_URL,
    ARTIFACTORY_USER,
    ARTIFACTORY_PASSWORD,
    GO_SERVER_URL,
    GO_PIPELINE_NAME,
    GO_PIPELINE_COUNTER,
    GO_STAGE_COUNTER,
    PIPELINE_VALUESTREAM_URL {
        @Override
        public String from(EnvironmentVariables variables) {
            String serverUrl = GO_SERVER_URL.from(variables);
            String pipelineName = GO_PIPELINE_NAME.from(variables);
            String pipelineCounter = GO_PIPELINE_COUNTER.from(variables);

            return format("%s%s/%s/%s/%s", serverUrl, "pipelines", "value_stream_map", pipelineName, pipelineCounter);
        }
    };

    public String from(EnvironmentVariables variables) {
        Map<String, String> envMap = variables.asMap();

        if (envMap.containsKey(name()))
            return envMap.get(name());

        throw new MissingEnvironmentDataException(name());
    }
}
