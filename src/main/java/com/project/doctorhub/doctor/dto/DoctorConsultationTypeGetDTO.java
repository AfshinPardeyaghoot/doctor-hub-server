package com.project.doctorhub.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorConsultationTypeGetDTO {

    private String id;
    private String title;
    private String name;
    private Long price;

}
