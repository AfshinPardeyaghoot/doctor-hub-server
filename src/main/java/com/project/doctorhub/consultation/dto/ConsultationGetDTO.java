package com.project.doctorhub.consultation.dto;

import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.doctor.dto.DoctorConsultationTypeGetDTO;
import com.project.doctorhub.doctor.dto.DoctorSlimDTO;
import com.project.doctorhub.user.dto.UserInfoGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationGetDTO {

    private String id;
    private Long price;
    private ConsultationTypeGetDTO consultationType;
    private DoctorSlimDTO doctor;
    private UserInfoGetDTO user;
    private Long createdAt;

}
