package com.tw.go.plugins.artifactory.model;

import com.google.common.collect.Maps;
import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.transformEntries;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.*;
import static java.lang.String.format;

public class GoBuildDetailsFactory {
    private static final String OBFUSCATED = "****";

    public GoBuildDetails createBuildDetails(EnvironmentVariables envVars, Collection<GoArtifact> buildArtifacts) {
        GoBuildDetails buildDetails =
                new GoBuildDetails(GO_PIPELINE_NAME.from(envVars), buildNumber(envVars), DateTime.now());

        buildDetails.url(PIPELINE_VALUESTREAM_URL.from(envVars));
        buildDetails.artifacts(buildArtifacts);
        buildDetails.environmentVariables(obfuscate(envVars));

        return buildDetails;
    }

    private String buildNumber(EnvironmentVariables envVars) {
        return format("%s.%s", GO_PIPELINE_COUNTER.from(envVars), GO_STAGE_COUNTER.from(envVars));
    }

    private Map<String, String> obfuscate(EnvironmentVariables envVars) {
        final Console.SecureEnvVarSpecifier secureEnvSpecifier = envVars.secureEnvSpecifier();

        return transformEntries(envVars.asMap(), new Maps.EntryTransformer<String, String, String>() {
            @Override
            public String transformEntry(String key, String value) {
                return secureEnvSpecifier.isSecure(key) ? OBFUSCATED : value;
            }
        });
    }
}
