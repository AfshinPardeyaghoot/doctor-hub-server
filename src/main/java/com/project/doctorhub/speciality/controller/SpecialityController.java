package com.project.doctorhub.speciality.controller;

import com.project.doctorhub.base.dto.HttpResponse;
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

    @PostMapping
    public ResponseEntity<HttpResponse<SpecialityGetDTO>> addSpeciality(
            @RequestBody SpecialityCreateDTO specialityCreateDTO
    ) {
        Speciality speciality = specialityService.create(specialityCreateDTO);
        return ResponseEntity.ok(new HttpResponse<>(specialityDTOMapper.entityToGetDTO(speciality)));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HttpResponse<SpecialityGetDTO>> updateSpeciality(
            @PathVariable String uuid,
            @RequestBody SpecialityCreateDTO createDTO
    ) {
        Speciality speciality = specialityService.update(uuid, createDTO);
        return ResponseEntity.ok(new HttpResponse<>(specialityDTOMapper.entityToGetDTO(speciality)));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<HttpResponse<Void>> deleteSpeciality(
            @PathVariable String uuid
    ) {
        specialityService.delete(uuid);
        return ResponseEntity.ok(HttpResponse.EMPTY_SUCCESS());
    }
}
