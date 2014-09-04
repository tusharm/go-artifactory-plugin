package com.tw.go.plugins.artifactory;

import java.io.IOException;
import java.util.Properties;

public class PluginProperties {
    private static final String PROPERTIES = "plugin.properties";
    private Properties properties;

    public PluginProperties() {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES));
        } catch (IOException e) {
            throw new RuntimeException("Error loading " + PROPERTIES, e);
        }
    }

    public String name() {
        return (String) properties.get("name");
    }

    public String version() {
        return (String) properties.get("version");
    }
}
