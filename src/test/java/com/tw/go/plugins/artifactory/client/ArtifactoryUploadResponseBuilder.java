package com.tw.go.plugins.artifactory.client;

import org.jfrog.build.client.ArtifactoryUploadResponse;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ArtifactoryUploadResponseBuilder {
    private String repo = "repo";
    private String path = "path";
    private String created = "01-01-2014";
    private String createdBy = "go";
    private String downloadUri = "http://artifactory/path/to/artifact.ext";
    private String mimeType = "application/json";
    private String size = "21";
    private String uri = "path/to/artifact.ext";
    private List<ArtifactoryUploadResponse.Error> errors = asList(new ErrorBuilder().build("message", "status"));
    private ArtifactoryUploadResponse.Checksums checksums = new ChecksumsBuilder().build("a", "b");
    private ArtifactoryUploadResponse.Checksums originalChecksums = new ChecksumsBuilder().build("c", "d");

    public ArtifactoryUploadResponse build() {
        ArtifactoryUploadResponse response = new ArtifactoryUploadResponse();
        response.setRepo(repo);
        response.setPath(path);
        response.setCreated(created);
        response.setCreatedBy(createdBy);
        response.setDownloadUri(downloadUri);
        response.setMimeType(mimeType);
        response.setSize(size);
        response.setUri(uri);
        response.setErrors(errors);
        response.setChecksums(checksums);
        response.setOriginalChecksums(originalChecksums);
        return response;
    }

    public ArtifactoryUploadResponseBuilder withErrors(List<ArtifactoryUploadResponse.Error> errors) {
        this.errors = errors;
        return this;
    }

    private static class ChecksumsBuilder {
        public ArtifactoryUploadResponse.Checksums build(String sha1, String md5) {
            ArtifactoryUploadResponse.Checksums checksums = new ArtifactoryUploadResponse.Checksums();
            checksums.setMd5(md5);
            checksums.setSha1(sha1);
            return checksums;
        }
    }

    private static class ErrorBuilder {
        public ArtifactoryUploadResponse.Error build(String message, String status) {
            ArtifactoryUploadResponse.Error error = new ArtifactoryUploadResponse.Error();
            error.setMessage(message);
            error.setStatus(status);
            return error;
        }
    }
}
