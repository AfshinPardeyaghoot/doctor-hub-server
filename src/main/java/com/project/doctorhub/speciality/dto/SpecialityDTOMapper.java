package com.project.doctorhub.speciality.dto;

import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.storageFile.dto.StorageFileDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SpecialityDTOMapper {

    private final StorageFileDTOMapper storageFileDTOMapper;

    public SpecialityGetDTO entityToGetDTO(Speciality entity) {
        SpecialityGetDTO dto = new SpecialityGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setFullTitle(entity.getFullTitle());
        dto.setImageDownloadUrl(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getImage()));
        return dto;
    }
}
