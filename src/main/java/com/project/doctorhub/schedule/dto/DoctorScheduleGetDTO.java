package com.project.doctorhub.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleGetDTO {

    private String day;
    private String startHour;
    private String endHour;

}
