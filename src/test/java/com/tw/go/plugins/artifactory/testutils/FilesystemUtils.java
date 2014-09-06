package com.tw.go.plugins.artifactory.testutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static org.apache.commons.lang.StringUtils.join;

public class FilesystemUtils {
    public static String path(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    public static String read(File file) throws IOException {
        try(InputStream stream = new FileInputStream(file)) {
            return join(IOUtils.readLines(stream), "\n").trim();
        }
    }

    public static void write(String path, String content) throws IOException {
        FileUtils.forceMkdir(Paths.get(path).getParent().toFile());

        try (OutputStream stream = new FileOutputStream(new File(path))) {
            IOUtils.write(content, stream, "UTF-8");
        }
    }

    public static void delete(File file) throws IOException {
        FileUtils.forceDelete(file);
    }
}
