package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.schedule.dto.DoctorScheduleUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateDTO {

    private String phone;
    private String firstname;
    private String lastname;
    private String description;
    private String gmcNumber;
    private MultipartFile profileImage;
    private String specialityId;
    private List<DoctorScheduleUpdateDTO> schedules = new ArrayList<>();

}
