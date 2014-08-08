package com.tw.go.plugins.artifactory.testutils;

import java.nio.file.Paths;

public class FilesystemUtils {
    public static String path(String first, String... more) {
        return Paths.get(first, more).toString();
    }
}
