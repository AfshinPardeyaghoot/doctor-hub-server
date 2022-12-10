package com.project.doctorhub.consultation.dto;

import com.project.doctorhub.doctor.dto.DoctorConsultationTypeGetDTO;
import com.project.doctorhub.doctor.model.DoctorConsultationType;
import org.springframework.stereotype.Component;

@Component
public class ConsultationTypeDTOMapper {

    public DoctorConsultationTypeGetDTO entityToGetDTO(DoctorConsultationType entity){
        DoctorConsultationTypeGetDTO dto = new DoctorConsultationTypeGetDTO();
        dto.setId(entity.getUUID());
        dto.setName(entity.getConsultationType().getName());
        dto.setTitle(entity.getConsultationType().getTitle());
        dto.setPrice(entity.getPrice());
        return dto;
    }

}
