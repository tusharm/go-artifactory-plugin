package com.tw.go.plugins.artifactory.utils;

import com.google.common.base.Function;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.List;

import static com.tw.go.plugins.artifactory.utils.Iterables.map;
import static java.io.File.separator;
import static java.lang.String.format;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

public class DirectoryScanner {
    private Path root;
    private final FileSystem fileSystem;

    public DirectoryScanner(String rootDirectory) {
        root = Paths.get(rootDirectory);
        fileSystem = FileSystems.getDefault();
    }

    public List<File> scan(String pattern)  {
        try {
            String globPattern = format("glob:%s%s%s", root, separator, pattern);

            PathMatchingVisitor visitor = new PathMatchingVisitor(fileSystem.getPathMatcher(globPattern));
            Files.walkFileTree(root, EnumSet.of(FOLLOW_LINKS), Integer.MAX_VALUE, visitor);

            return (List<File>) map(visitor.matched(), new Function<Path, File>() {
                @Override
                public File apply(Path path) {
                    return path.toFile();
                }
            });
        }
        catch(IOException iox) {
            throw new RuntimeException(iox);
        }
    }
}