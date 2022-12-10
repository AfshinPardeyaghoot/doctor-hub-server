package com.project.doctorhub.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryGetDTO {

    private String id;
    private String name;
    private String title;
    private String fullTitle;
    private String description;
    private String imageDownloadUrl;

}
