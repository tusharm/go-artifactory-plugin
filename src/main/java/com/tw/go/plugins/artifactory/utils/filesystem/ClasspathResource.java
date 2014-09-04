package com.tw.go.plugins.artifactory.utils.filesystem;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClasspathResource {
    private static final String UTF_8 = "UTF-8";

    public String read(String resourcePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

        try (InputStreamReader reader = new InputStreamReader(inputStream, UTF_8)) {
            return CharStreams.toString(reader);
        }
    }
}
