package com.project.doctorhub.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {

    private String title;
    private String fullTitle;
    private String description;
    private MultipartFile image;
    private List<String> specialityIds;

}
