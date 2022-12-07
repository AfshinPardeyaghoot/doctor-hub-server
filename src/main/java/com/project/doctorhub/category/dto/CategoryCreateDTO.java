package com.project.doctorhub.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDTO {

    @NotEmpty(message = "لطفا نام تخصص را وارد کنید!")
    private String name;

    @NotEmpty(message = "لطفا عنوان تخصص را وارد کنید!")
    private String title;

    @NotEmpty(message = "لطفا عنوان کامل را وارد کنید!")
    private String fullTitle;

    @NotEmpty(message = "لطفا توضیحات را وارد کنید!")
    private String description;

    @NotNull(message = "لطفا عکس تخصص را وارد کنید!")
    private MultipartFile image;

    private List<String> specialityIds;

}
