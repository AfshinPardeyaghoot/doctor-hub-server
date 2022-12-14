package com.project.doctorhub.schedule.dto;

import com.project.doctorhub.schedule.model.DoctorSchedule;
import org.springframework.stereotype.Component;

@Component
public class DoctorScheduleDTOMapper {

    public DoctorScheduleGetDTO entityToGetDTO(DoctorSchedule entity){
        DoctorScheduleGetDTO dto = new DoctorScheduleGetDTO();
        dto.setDay(entity.getDay().getSolarValue());
        dto.setStartHour(entity.getStartHour());
        dto.setEndHour(entity.getEndHour());
        return dto;
    }

}
