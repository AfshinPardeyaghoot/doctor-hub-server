package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.consultation.dto.ConsultationTypeDTOMapper;
import com.project.doctorhub.doctor.model.Doctor;
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
    private final ConsultationTypeDTOMapper consultationTypeDTOMapper;


    public DoctorGetDTO entityToGetDTO(Doctor entity) {
        DoctorGetDTO dto = new DoctorGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setSpeciality(specialityDTOMapper.entityToGetDTO(entity.getSpeciality()));
        dto.setProfileImage(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getProfileImage()));
        dto.setConsultationTypes(
                entity.getDoctorConsultationTypes().stream()
                        .filter(doctorConsultationType -> !doctorConsultationType.getIsDeleted())
                        .map(consultationTypeDTOMapper::entityToGetDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }


}
