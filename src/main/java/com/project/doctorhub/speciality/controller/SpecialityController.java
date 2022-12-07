package com.project.doctorhub.speciality.controller;

import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/speciality")
public class SpecialityController {

    private final SpecialityService specialityService;

}
