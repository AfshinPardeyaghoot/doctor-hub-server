package com.project.doctorhub.category.dto;

import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.storageFile.dto.StorageFileDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryDTOMapper {

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
}
