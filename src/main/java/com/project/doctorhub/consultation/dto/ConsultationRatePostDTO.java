package com.project.doctorhub.consultation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRatePostDTO {

    private String consultationId;
    private Integer rate;
}
