package com.project.doctorhub.schedule.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.schedule.dto.DoctorScheduleAddDTO;
import com.project.doctorhub.schedule.dto.DoctorScheduleDTOMapper;
import com.project.doctorhub.schedule.dto.DoctorScheduleGetDTO;
import com.project.doctorhub.schedule.model.DoctorSchedule;
import com.project.doctorhub.schedule.service.DoctorScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/schedule/doctor")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleDTOMapper doctorScheduleDTOMapper;

    public ResponseEntity<HttpResponse<DoctorScheduleGetDTO>> addDoctorSchedule(
            @Valid @RequestBody DoctorScheduleAddDTO doctorScheduleAddDTO
    ){
        DoctorSchedule doctorSchedule = doctorScheduleService.create(doctorScheduleAddDTO);
        DoctorScheduleGetDTO doctorScheduleGetDTO = doctorScheduleDTOMapper.entityToGetDTO(doctorSchedule);
        return ResponseEntity.ok(new HttpResponse<>(doctorScheduleGetDTO));
    }

    public ResponseEntity<?> deleteDoctorSchedule(){
        return null;
    }

    public ResponseEntity<?> getDoctorSchedules(){
        return null;
    }

    public ResponseEntity<?> updateDoctorSchedule(){
        return null;
    }

}
