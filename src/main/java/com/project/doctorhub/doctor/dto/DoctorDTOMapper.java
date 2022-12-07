package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DoctorDTOMapper {

    private final SpecialityDTOMapper specialityDTOMapper;


    public DoctorGetDTO entityToGetDTO(Doctor entity) {
        DoctorGetDTO dto = new DoctorGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setSpeciality(specialityDTOMapper.entityToGetDTO(entity.getSpeciality()));
        return dto;
    }


}
