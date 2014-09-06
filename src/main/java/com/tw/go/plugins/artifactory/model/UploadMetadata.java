package com.tw.go.plugins.artifactory.model;

import com.tw.go.plugins.artifactory.Logger;
import com.tw.go.plugins.artifactory.task.publish.Publishable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jfrog.build.client.ArtifactoryUploadResponse;

import java.io.IOException;
import java.util.Collection;

public class UploadMetadata implements Publishable {
    private Logger logger = Logger.getLogger(getClass());
    private ObjectMapper jsonMapper;

    private Collection<ArtifactoryUploadResponse> metadata;

    public UploadMetadata(Collection<ArtifactoryUploadResponse> metadata) {
        this.metadata = metadata;
        this.jsonMapper = new ObjectMapper();
    }

    @Override
    public String name() {
        return "uploadMetadata.json";
    }

    @Override
    public String content() {
        try {
            return jsonMapper
                    .setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL)
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(metadata);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error creating build artifact json: " + e.getMessage(), e);
        }
    }

    public Collection<ArtifactoryUploadResponse> getMetadata() {
        return metadata;
    }
}
