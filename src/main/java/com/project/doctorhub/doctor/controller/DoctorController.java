package com.project.doctorhub.doctor.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.doctor.dto.*;

import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.service.DoctorService;
import com.project.doctorhub.schedule.dto.DoctorScheduleUpdateDTO;
import com.project.doctorhub.schedule.service.DoctorAvailableDayService;
import com.project.doctorhub.util.ListUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Predicate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final ListUtil listUtil;
    private final DoctorService doctorService;
    private final DoctorDTOMapper doctorDTOMapper;
    private final DoctorAvailableDayService doctorAvailableDayService;

    @PostMapping
    public ResponseEntity<HttpResponse<DoctorGetDTO>> createDoctor(
            @Valid @ModelAttribute DoctorCreateDTO doctorCreateDTO
    ) {
        Doctor doctor = doctorService.create(doctorCreateDTO);
        DoctorGetDTO doctorGetDTO = doctorDTOMapper.entityToGetDTO(doctor);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTO));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HttpResponse<DoctorGetDTO>> updateDoctor(
            @PathVariable String uuid,
            @ModelAttribute DoctorUpdateDTO doctorUpdateDTO
    ) {

        Doctor doctor = doctorService.update(uuid, doctorUpdateDTO);
        DoctorGetDTO doctorGetDTO = doctorDTOMapper.entityToGetDTO(doctor);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTO));
    }

    @PutMapping("/{uuid}/schedules")
    public ResponseEntity<HttpResponse<DoctorGetDTO>> updateDoctor(
            @PathVariable String uuid,
            @RequestBody DoctorSchedulesUpdateDTO schedules
    ) {
        Doctor doctor = doctorService.updateDoctorSchedules(uuid, schedules.getSchedules());
        DoctorGetDTO doctorGetDTO = doctorDTOMapper.entityToGetDTO(doctor);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTO));
    }

    @GetMapping
    public ResponseEntity<HttpResponse<Page<DoctorGetDTO>>> getDoctors(
            @RequestParam(required = false) String name,
            @PageableDefault Pageable pageable
    ) {
        List<Doctor> doctors = doctorService.findAllByNameLike();
        Predicate<DoctorGetDTO> filterName = (doctor -> name == null || (doctor.getName().contains(name)));
        return ResponseEntity.ok(new HttpResponse<>(listUtil.getPage(pageable, doctors.stream().map(doctorDTOMapper::entityToGetDTO).filter(filterName).toList())));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HttpResponse<DoctorGetDTO>> getDoctorByUUID(
            @PathVariable String uuid
    ) {
        Doctor doctor = doctorService.findByUUIDNotDeleted(uuid);
        boolean isOnline = doctorAvailableDayService.isDoctorAvailableNow(doctor);
        DoctorGetDTO doctorGetDTO = doctorDTOMapper.entityToGetDTO(doctor, isOnline);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTO));
    }

    @GetMapping("/{uuid}/full")
    public ResponseEntity<HttpResponse<DoctorFullDTO>> getFullDoctorByUUID(
            @PathVariable String uuid
    ) {
        Doctor doctor = doctorService.findByUUIDNotDeleted(uuid);
        DoctorFullDTO doctorFullDTO = doctorDTOMapper.entityToFullDTO(doctor);
        return ResponseEntity.ok(new HttpResponse<>(doctorFullDTO));
    }

}
