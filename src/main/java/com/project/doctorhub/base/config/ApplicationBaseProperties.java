package com.project.doctorhub.base.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationBaseProperties {
    private final String storageBasePath;
    private final String downloadFileApi;

    public ApplicationBaseProperties(
            @Value("${storage.base.path}") String storageBasePath,
            @Value("${download.file.api.address}") String downloadFileApi
    ) {
        this.storageBasePath = storageBasePath;
        this.downloadFileApi = downloadFileApi;
    }
}
