package com.project.doctorhub.doctor.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.doctor.dto.DoctorDTOMapper;
import com.project.doctorhub.doctor.dto.DoctorGetDTO;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.dto.DoctorCreateDTO;
import com.project.doctorhub.doctor.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorDTOMapper doctorDTOMapper;

    @PostMapping
    public ResponseEntity<HttpResponse<DoctorGetDTO>> createDoctor(
            @Valid @ModelAttribute DoctorCreateDTO doctorCreateDTO
    ) {
        Doctor doctor = doctorService.create(doctorCreateDTO);
        DoctorGetDTO doctorGetDTO = doctorDTOMapper.entityToGetDTO(doctor);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTO));
    }


    @GetMapping
    public ResponseEntity<?> getDoctors() {
        return null;
    }
}
