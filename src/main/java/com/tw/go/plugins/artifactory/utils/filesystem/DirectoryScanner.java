package com.tw.go.plugins.artifactory.utils.filesystem;

import com.google.common.base.Function;
import com.tw.go.plugins.artifactory.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static com.google.common.collect.Collections2.transform;
import static java.io.File.separator;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static java.util.Collections.EMPTY_SET;

public class DirectoryScanner {
    private Logger logger = Logger.getLogger(this.getClass());

    private Path root;
    private final FileSystem fileSystem;

    public DirectoryScanner(String rootDirectory) {
        root = Paths.get(rootDirectory);
        fileSystem = FileSystems.getDefault();
    }

    public Collection<File> scan(String pattern) {
        try {
            String globPattern = format("glob:%s/%s", root, pattern).replace(separator, "/");

            PathMatchingVisitor visitor = new PathMatchingVisitor(fileSystem.getPathMatcher(globPattern));
            Files.walkFileTree(root, EMPTY_SET, MAX_VALUE, visitor);
            return matchingFilesFrom(visitor);
        }
        catch(IOException iox) {
            throw new RuntimeException(iox);
        }
    }


    private Collection<File> matchingFilesFrom(PathMatchingVisitor visitor) {
        logger.debug("Scanned files:");

        return transform(visitor.matched(), new Function<Path, File>() {
            @Override
            public File apply(Path path) {
                logger.debug(path.toString());
                return path.toFile();
            }
        });
    }
}