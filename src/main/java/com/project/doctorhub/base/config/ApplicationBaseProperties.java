package com.project.doctorhub.base.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationBaseProperties {
    private final String storageBasePath;

    public ApplicationBaseProperties(
            @Value("${storage.base.path}") String storageBasePath
    ) {
        this.storageBasePath = storageBasePath;
    }
}
