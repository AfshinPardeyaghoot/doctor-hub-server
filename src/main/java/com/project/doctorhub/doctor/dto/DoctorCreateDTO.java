package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.schedule.dto.DoctorScheduleUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateDTO {



    private String firstname;
    private String lastname;
    @NotEmpty(message = "لطفا شماره تلفن پزشک را وارد کنید!")
    private String phone;

    @NotEmpty(message = "لطفا توضیحات را وارد کنید!")
    private String description;

    @NotEmpty(message = " لطفا شماره نظام پزشکی پزشک را وارد کنید!")
    private String gmcNumber;

    @NotNull(message = "لطفا عکس پروفایل پزشک را وارد کنید!")
    private MultipartFile profileImage;

    @NotNull(message = "لطفا تخصص پزشک را انتخاب نمایید")
    private String specialityId;

    private List<DoctorScheduleUpdateDTO> schedules = new ArrayList<>();
}
