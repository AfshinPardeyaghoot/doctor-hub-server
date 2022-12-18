package com.project.doctorhub.consultation.dto;

import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.doctor.dto.DoctorDTOMapper;
import com.project.doctorhub.user.dto.UserDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultationDTOMapper {

    private final UserDTOMapper userDTOMapper;
    private final DoctorDTOMapper doctorDTOMapper;


    public ConsultationGetDTO entityToGetDTO(Consultation entity) {
        ConsultationGetDTO dto = new ConsultationGetDTO();
        dto.setId(entity.getUUID());
        dto.setPrice(entity.getPrice());
        dto.setDoctor(doctorDTOMapper.entityToSlimDTO(entity.getDoctor()));
        dto.setUser(userDTOMapper.entityToInfoDTO(entity.getUser()));
        dto.setCreatedAt(entity.getCreatedAt().toEpochMilli());
        return dto;
    }

    public ConsultationTypeGetDTO entityToGetDTO(ConsultationType entity) {
        ConsultationTypeGetDTO dto = new ConsultationTypeGetDTO();
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        return dto;
    }

}
