package com.project.doctorhub.speciality.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialityUpdateDTO {

    private String title;
    private String fullTitle;
    private String description;
    private MultipartFile image;
}
