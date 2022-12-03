package com.project.doctorhub.storageFile.dto;

import com.project.doctorhub.base.config.ApplicationBaseProperties;
import com.project.doctorhub.storageFile.model.StorageFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StorageFileDTOMapper {

    private final ApplicationBaseProperties applicationBaseProperties;

    public String getStorageFileDownloadUrl(StorageFile entity) {
        return String.format("%s/%s", applicationBaseProperties.getDownloadFileApi(), entity.getUUID());
    }
}
