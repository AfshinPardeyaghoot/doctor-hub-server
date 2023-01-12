package com.project.doctorhub.consultation.dto;

import com.project.doctorhub.chat.dto.ChatDTOMapper;
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
    private final ChatDTOMapper chatDTOMapper;
    private final DoctorDTOMapper doctorDTOMapper;


    public ConsultationGetDTO entityToGetDTO(Consultation entity) {
        ConsultationGetDTO dto = new ConsultationGetDTO();
        dto.setId(entity.getUUID());
        dto.setPrice(entity.getPrice());
        dto.setStatusType(entity.getStatus());
        dto.setStatus(entity.getStatus().getPersian());
        dto.setConsultationType(entityToGetDTO(entity.getConsultationType()));
        dto.setDoctor(doctorDTOMapper.entityToSlimDTO(entity.getDoctor()));
        dto.setUser(userDTOMapper.entityToInfoDTO(entity.getUser()));
        if (entity.getChat() != null)
            dto.setChat(chatDTOMapper.entityToGetDTO(entity.getChat()));
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
