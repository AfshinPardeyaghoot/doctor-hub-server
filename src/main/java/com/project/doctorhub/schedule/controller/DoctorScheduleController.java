package com.project.doctorhub.schedule.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.schedule.dto.DoctorScheduleAddDTO;
import com.project.doctorhub.schedule.dto.DoctorScheduleDTOMapper;
import com.project.doctorhub.schedule.dto.DoctorScheduleDeleteDTO;
import com.project.doctorhub.schedule.dto.DoctorScheduleGetDTO;
import com.project.doctorhub.schedule.model.DoctorSchedule;
import com.project.doctorhub.schedule.service.DoctorScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/schedule/doctor")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleDTOMapper doctorScheduleDTOMapper;

    @PostMapping
    public ResponseEntity<HttpResponse<DoctorScheduleGetDTO>> addDoctorSchedule(
            @Valid @RequestBody DoctorScheduleAddDTO doctorScheduleAddDTO
    ) {
        DoctorSchedule doctorSchedule = doctorScheduleService.create(doctorScheduleAddDTO);
        DoctorScheduleGetDTO doctorScheduleGetDTO = doctorScheduleDTOMapper.entityToGetDTO(doctorSchedule);
        return ResponseEntity.ok(new HttpResponse<>(doctorScheduleGetDTO));
    }

    @DeleteMapping
    public ResponseEntity<HttpResponse<String>> deleteDoctorSchedule(
            @Valid @RequestBody DoctorScheduleDeleteDTO doctorScheduleDeleteDTO
    ) {
        doctorScheduleService.delete(doctorScheduleDeleteDTO);
        return ResponseEntity.ok(new HttpResponse<>("success"));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HttpResponse<List<DoctorScheduleGetDTO>>> getDoctorSchedules(
            @PathVariable String uuid
    ) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleService.findAllByDoctorNotDeleted(uuid);
        List<DoctorScheduleGetDTO> doctorScheduleGetDTOS = doctorSchedules.stream().map(doctorScheduleDTOMapper::entityToGetDTO).toList();
        return ResponseEntity.ok(new HttpResponse<>(doctorScheduleGetDTOS));
    }

    @PutMapping
    public ResponseEntity<HttpResponse<DoctorScheduleGetDTO>> updateDoctorSchedule(
            @Valid @RequestBody DoctorScheduleAddDTO doctorScheduleAddDTO
    ) {
        DoctorSchedule doctorSchedule = doctorScheduleService.update(doctorScheduleAddDTO);
        DoctorScheduleGetDTO doctorScheduleGetDTO = doctorScheduleDTOMapper.entityToGetDTO(doctorSchedule);
        return ResponseEntity.ok(new HttpResponse<>(doctorScheduleGetDTO));
    }

}
