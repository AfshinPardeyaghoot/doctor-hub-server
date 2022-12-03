package com.project.doctorhub.speciality.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/speciality")
public class SpecialityController {

    private final SpecialityService specialityService;
    private final SpecialityDTOMapper specialityDTOMapper;

    @PostMapping
    public ResponseEntity<HttpResponse<SpecialityGetDTO>> createSpeciality(
            @Valid @ModelAttribute SpecialityCreateDTO specialityCreateDTO
    ) {
        System.out.println("User authenticated");
        Speciality speciality = specialityService.create(specialityCreateDTO);
        SpecialityGetDTO specialityGetDTO = specialityDTOMapper.entityToGetDTO(speciality);
        return ResponseEntity.ok(new HttpResponse<>(specialityGetDTO));
    }
}
