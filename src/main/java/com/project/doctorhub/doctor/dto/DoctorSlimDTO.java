package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSlimDTO {

    private String id;
    private String name;
    private String gmcNumber;
    private String profileImage;
    private SpecialityGetDTO speciality;

}
