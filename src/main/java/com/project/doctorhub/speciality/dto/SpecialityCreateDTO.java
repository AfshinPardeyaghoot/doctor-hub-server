package com.project.doctorhub.speciality.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialityCreateDTO {

    @NotEmpty(message = "لطفا نام تخصص را وارد کنید!")
    private String name;

    @NotEmpty(message = "لطفا عنوان تخصص را وارد کنید!")
    private String title;

    @NotNull(message = "لطفا عکس تخصص را وارد کنید!")
    private MultipartFile image;

}
