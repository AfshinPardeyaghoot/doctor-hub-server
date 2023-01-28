package com.project.doctorhub.speciality.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.speciality.dto.SpecialityDTOMapper;
import com.project.doctorhub.speciality.dto.SpecialityGetDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/speciality")
public class SpecialityController {

    private final SpecialityService specialityService;
    private final SpecialityDTOMapper specialityDTOMapper;

    @GetMapping
    public ResponseEntity<HttpResponse<Page<SpecialityGetDTO>>> getSpecialities(
            @RequestParam(required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        Page<Speciality> specialities = specialityService.findAllBySearchNotDeleted(search, pageable);
        return ResponseEntity.ok(new HttpResponse<>(specialities.map(specialityDTOMapper::entityToGetDTO)));
    }

}
