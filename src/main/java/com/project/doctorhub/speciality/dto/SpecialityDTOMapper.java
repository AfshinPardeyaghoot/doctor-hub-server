package com.project.doctorhub.speciality.dto;

import com.project.doctorhub.speciality.model.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityDTOMapper {

    public SpecialityGetDTO entityToGetDTO(Speciality entity) {
        SpecialityGetDTO dto = new SpecialityGetDTO();
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());

        return dto;
    }
}
