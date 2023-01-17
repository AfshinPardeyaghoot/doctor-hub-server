package com.project.doctorhub.doctor.dto;

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
    private final DoctorConsultationTypeDTOMapper doctorConsultationTypeDTOMapper;

    public DoctorGetDTO entityToGetDTO(Doctor entity, boolean isOnline) {
        DoctorGetDTO dto = new DoctorGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setRate(entity.getRate());
        dto.setOnline(isOnline);
        dto.setConsultationCount(entity.getConsultationCount());
        dto.setSpeciality(specialityDTOMapper.entityToGetDTO(entity.getSpeciality()));
        dto.setProfileImage(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getProfileImage()));
        dto.setConsultationTypes(
                entity.getDoctorConsultationTypes().stream()
                        .filter(doctorConsultationType -> !doctorConsultationType.getIsDeleted())
                        .map(doctorConsultationTypeDTOMapper::entityToGetDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    public DoctorGetDTO entityToGetDTO(Doctor entity) {
        DoctorGetDTO dto = new DoctorGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setRate(entity.getRate());
        dto.setOnline(false);
        dto.setConsultationCount(entity.getConsultationCount());
        dto.setSpeciality(specialityDTOMapper.entityToGetDTO(entity.getSpeciality()));
        dto.setProfileImage(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getProfileImage()));
        dto.setConsultationTypes(
                entity.getDoctorConsultationTypes().stream()
                        .filter(doctorConsultationType -> !doctorConsultationType.getIsDeleted())
                        .map(doctorConsultationTypeDTOMapper::entityToGetDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    public DoctorSlimDTO entityToSlimDTO(Doctor entity) {
        DoctorSlimDTO dto = new DoctorSlimDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getUser().getUsername());
        dto.setGmcNumber(entity.getGmcNumber());
        dto.setRate(entity.getRate());
        dto.setConsultationCount(entity.getConsultationCount());
        dto.setSpeciality(specialityDTOMapper.entityToGetDTO(entity.getSpeciality()));
        dto.setProfileImage(storageFileDTOMapper.getStorageFileDownloadUrl(entity.getProfileImage()));
        return dto;
    }


}
