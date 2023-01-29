package com.project.doctorhub.category.dto;

import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFullDTO {

    private String id;
    private String name;
    private String title;
    private String fullTitle;
    private String description;
    private String imageDownloadUrl;
    private List<SpecialityGetDTO> specialities = new ArrayList<>();
}
