package com.project.doctorhub.consultation.dto;

import com.project.doctorhub.chat.dto.ChatGetDTO;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.consultation.model.ConsultationType;
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
    private ChatGetDTO chat;
    private UserInfoGetDTO user;
    private String status;
    private ConsultationStatusType statusType;
    private Long createdAt;

}
