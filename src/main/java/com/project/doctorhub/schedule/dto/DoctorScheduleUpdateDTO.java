package com.project.doctorhub.schedule.dto;


import com.project.doctorhub.schedule.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleUpdateDTO {

    @NotNull(message = "لطفا یک روز از هفته را انتخاب کنید!")
    private DayOfWeek day;

    @NotEmpty(message = "لطفا ساعت شروع را وارد کنید!")
    private String startHour;

    @NotEmpty(message = "لطفا ساعت پابان را انتخاب کنید!")
    private String endHour;

}
