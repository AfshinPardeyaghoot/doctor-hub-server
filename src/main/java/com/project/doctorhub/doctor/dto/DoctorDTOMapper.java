package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorSpeciality;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import com.project.doctorhub.storageFile.dto.StorageFileDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DoctorDTOMapper {

    private final SpecialityDTOMapper specialityDTOMapper;
    private final StorageFileDTOMapper storageFileDTOMapper;


    public DoctorGetDTO entityToGetDTO(Doctor entity) {
        DoctorGetDTO dto = new DoctorGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setProfileImage(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getProfileImage()));
        dto.setSpecialities(
                entity.getDoctorSpecialities().stream()
                        .map(DoctorSpeciality::getSpeciality)
                        .map(specialityDTOMapper::entityToGetDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }


}
