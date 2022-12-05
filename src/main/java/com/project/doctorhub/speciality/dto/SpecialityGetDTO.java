package com.project.doctorhub.speciality.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialityGetDTO {

    private String name;
    private String title;
    private String fullTitle;
    private String description;
    private String imageDownloadUrl;

}
