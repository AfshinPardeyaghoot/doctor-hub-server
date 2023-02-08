package com.project.doctorhub.doctor.dto;

import com.project.doctorhub.schedule.dto.DoctorScheduleUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedulesUpdateDTO {

    List<DoctorScheduleUpdateDTO> schedules = new ArrayList<>();
}
