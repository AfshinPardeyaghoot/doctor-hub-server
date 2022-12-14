package com.project.doctorhub.schedule.dto;

import com.project.doctorhub.schedule.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDeleteDTO {

    @NotNull(message = "لطفا شماره تلفن پزشک را وارد کنید!")
    private String doctorPhone;

    @NotNull(message = "لطفا یک روز از هفته را انتخاب کنید!")
    private DayOfWeek day;

}
