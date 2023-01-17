package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorGetDTO {

    private String id;
    private String name;
    private String description;
    private Float rate;
    private Integer consultationCount;
    private String gmcNumber;
    private String profileImage;
    private boolean isOnline;
    private SpecialityGetDTO speciality;
    private Set<DoctorConsultationTypeGetDTO> consultationTypes;

}
