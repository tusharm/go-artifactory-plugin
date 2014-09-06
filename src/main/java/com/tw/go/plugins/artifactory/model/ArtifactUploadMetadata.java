package com.tw.go.plugins.artifactory.model;

import com.google.common.base.Function;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.jfrog.build.client.ArtifactoryUploadResponse;

import java.util.List;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@JsonPropertyOrder({"downloadUri", "uri", "repo", "path", "mimeType", "size", "sha1", "originalSha1", "md5", "originalMd5", "created", "createdBy", "errors"})
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
        Iterable<String> transform = transform(response.getErrors(), new Function<ArtifactoryUploadResponse.Error, String>() {
            @Override
            public String apply(ArtifactoryUploadResponse.Error error) {
                return format("%s: %s", error.getStatus(), error.getMessage());
            }
        });
        return newArrayList(transform);
    }

    public String getSha1() {
        ArtifactoryUploadResponse.Checksums checksums = response.getChecksums();
        return checksums == null ? null : checksums.getSha1();
    }

    public String getMd5() {
        ArtifactoryUploadResponse.Checksums checksums = response.getChecksums();
        return checksums == null ? null : checksums.getMd5();
    }

    public String getOriginalSha1() {
        ArtifactoryUploadResponse.Checksums checksums = response.getOriginalChecksums();
        return checksums == null ? null : checksums.getSha1();
    }

    public String getOriginalMd5() {
        ArtifactoryUploadResponse.Checksums checksums = response.getOriginalChecksums();
        return checksums == null ? null : checksums.getMd5();
    }

    public String getUri() {
        return response.getUri();
    }

}
