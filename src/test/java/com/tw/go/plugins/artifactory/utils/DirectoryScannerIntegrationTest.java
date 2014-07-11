package com.tw.go.plugins.artifactory.utils;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.truth0.Truth.ASSERT;

public class DirectoryScannerIntegrationTest {
    private static Path rootDir;
    private static Path subDir;

    private static DirectoryScanner directoryScanner;

    @BeforeClass
    public static void beforeAll() throws IOException {
        rootDir = Files.createTempDirectory(null);
        subDir = Files.createTempDirectory(rootDir, null);

        directoryScanner = new DirectoryScanner(rootDir.toString());
    }

    @Test
    public void shouldReturnAlLFiles() throws IOException {
        Path temp1 = Files.createTempFile(rootDir, null, ".java");
        Path temp2 = Files.createTempFile(rootDir, null, ".java");

        List<File> files = directoryScanner.scan("*");

        ASSERT.that(files).has().exactly(temp1.toFile(), temp2.toFile());
    }

    @Test
    public void shouldReturnFilesMatchingGlob() throws IOException {
        Files.createTempFile(rootDir, null, ".java");
        Files.createTempFile(rootDir, null, ".java");
        Path temp = Files.createTempFile(subDir, null, ".class");

        List<File> files = directoryScanner.scan("**/*.class");

        ASSERT.that(files).has().exactly(temp.toFile());
    }

    @AfterClass
    public static void afterAll() throws IOException {
        FileUtils.deleteDirectory(rootDir.toFile());
    }
}