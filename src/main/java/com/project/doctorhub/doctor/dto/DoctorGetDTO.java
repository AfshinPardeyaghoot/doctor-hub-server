package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorGetDTO {

    private String id;
    private String name;
    private String description;
    private String gmcNumber;
    private String profileImage;
    private List<SpecialityGetDTO> specialities;

}
