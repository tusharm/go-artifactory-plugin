package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;
import org.jfrog.build.client.ArtifactoryUploadResponse;

import java.util.List;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

public class ArtifactUploadMetadata {
    private ArtifactoryUploadResponse response;

    public ArtifactUploadMetadata(ArtifactoryUploadResponse response) {
        this.response = response;
    }

    public String getRepo() {
        return response.getRepo();
    }

    public String getPath() {
        return response.getPath();
    }

    public String getCreated() {
        return response.getCreated();
    }

    public String getCreatedBy() {
        return response.getCreatedBy();
    }

    public String getDownloadUri() {
        return response.getDownloadUri();
    }

    public String getMimeType() {
        return response.getMimeType();
    }

    public String getSize() {
        return response.getSize();
    }

    public List<String> getErrors() {
        return newArrayList(transform(response.getErrors(), new Function<ArtifactoryUploadResponse.Error, String>() {
            @Override
            public String apply(ArtifactoryUploadResponse.Error error) {
                return format("[%s] %s", error.getStatus(), error.getMessage());
            }
        }));
    }

    public String getChecksums() {
        return prettyPrint(response.getChecksums());
    }

    public String getOriginalChecksums() {
        return prettyPrint(response.getOriginalChecksums());
    }

    private String prettyPrint(ArtifactoryUploadResponse.Checksums checksums) {
        return format("SHA1 => [%s], MD5 => [%s]", checksums.getSha1(), checksums.getMd5());
    }
}
