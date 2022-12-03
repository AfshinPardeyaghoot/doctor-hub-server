package com.project.doctorhub.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateDTO {

    private String description;
    private String gmcNumber;
    private MultipartFile profileImage;

}
