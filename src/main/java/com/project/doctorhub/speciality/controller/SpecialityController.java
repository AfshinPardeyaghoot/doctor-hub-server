package com.project.doctorhub.speciality.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.doctor.dto.DoctorDTOMapper;
import com.project.doctorhub.doctor.dto.DoctorGetDTO;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import com.project.doctorhub.speciality.dto.SpecialityUpdateDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/speciality")
public class SpecialityController {

    private final DoctorDTOMapper doctorDTOMapper;
    private final SpecialityService specialityService;
    private final SpecialityDTOMapper specialityDTOMapper;

    @PostMapping
    public ResponseEntity<HttpResponse<SpecialityGetDTO>> createSpeciality(
            @Valid @ModelAttribute SpecialityCreateDTO specialityCreateDTO
    ) {
        Speciality speciality = specialityService.create(specialityCreateDTO);
        SpecialityGetDTO specialityGetDTO = specialityDTOMapper.entityToGetDTO(speciality);
        return ResponseEntity.ok(new HttpResponse<>(specialityGetDTO));
    }
    @PutMapping("/{uuid}")
    public ResponseEntity<HttpResponse<SpecialityGetDTO>> updateSpeciality(
        @PathVariable String uuid,
        @ModelAttribute SpecialityUpdateDTO specialityUpdateDTO
    ){
        Speciality speciality = specialityService.update(uuid, specialityUpdateDTO);
        SpecialityGetDTO specialityGetDTO = specialityDTOMapper.entityToGetDTO(speciality);
        return ResponseEntity.ok(new HttpResponse<>(specialityGetDTO));
    }

    @GetMapping("/{uuid}/doctors")
    public ResponseEntity<HttpResponse<Page<DoctorGetDTO>>> getSpecialityDoctors(
            @PathVariable String uuid,
            @RequestParam(required = false) String doctorName,
            @PageableDefault Pageable pageable
    ) {
        Page<Doctor> doctors = specialityService.findAllSpecialityDoctorsByNameLike(uuid, doctorName, pageable);
        Page<DoctorGetDTO> doctorGetDTOS = doctors.map(doctorDTOMapper::entityToGetDTO);
        return ResponseEntity.ok(new HttpResponse<>(doctorGetDTOS));
    }

    @GetMapping
    public ResponseEntity<HttpResponse<Page<SpecialityGetDTO>>> getAllSpecialities(
            @PageableDefault Pageable pageable
    ) {
        Page<Speciality> specialities = specialityService.findAllNotDeleted(pageable);
        Page<SpecialityGetDTO> specialityGetDTOS = specialities.map(specialityDTOMapper::entityToGetDTO);
        return ResponseEntity.ok(new HttpResponse<>(specialityGetDTOS));
    }
}
