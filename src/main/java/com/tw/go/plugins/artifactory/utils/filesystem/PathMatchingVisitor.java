package com.tw.go.plugins.artifactory.utils.filesystem;

import com.tw.go.plugins.artifactory.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.lang.String.format;

public class PathMatchingVisitor extends SimpleFileVisitor<Path> {
    private Logger logger = Logger.getLogger(getClass());

    private PathMatcher pathMatcher;
    private Set<Path> matched;

    public PathMatchingVisitor(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        this.matched = new HashSet<>();
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        if (pathMatcher.matches(path))
            matched.add(path);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException exception) throws IOException {
        logger.error(format("Error processing file [%s]", path), exception);
        return super.visitFileFailed(path, exception);
    }

    public Collection<Path> matched() {
        return matched;
    }
}
