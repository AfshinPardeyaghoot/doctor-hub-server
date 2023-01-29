package com.project.doctorhub.category.dto;

import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.storageFile.dto.StorageFileDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryDTOMapper {

    private final SpecialityDTOMapper specialityDTOMapper;
    private final StorageFileDTOMapper storageFileDTOMapper;

    public CategoryGetDTO entityToGetDTO(Category entity) {
        CategoryGetDTO dto = new CategoryGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setFullTitle(entity.getFullTitle());
        dto.setImageDownloadUrl(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getImage()));
        return dto;
    }

    public CategoryFullDTO
    entityToFullDTO(Category entity, List<Speciality> specialities){
        CategoryFullDTO dto = new CategoryFullDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setFullTitle(entity.getFullTitle());
        dto.setImageDownloadUrl(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getImage()));
        dto.setSpecialities(specialities.stream().map(specialityDTOMapper::entityToGetDTO).toList());
        return dto;
    }
}
