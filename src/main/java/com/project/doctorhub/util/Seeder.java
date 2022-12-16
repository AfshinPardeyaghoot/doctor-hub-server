package com.project.doctorhub.util;

import com.project.doctorhub.category.service.CategoryService;
import com.project.doctorhub.consultation.service.ConsultationTypeService;
import com.project.doctorhub.doctor.service.DoctorService;
import com.project.doctorhub.speciality.service.SpecialityCategoryService;
import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final DoctorService doctorService;
    private final CategoryService categoryService;
    private final SpecialityService specialityService;
    private final ConsultationTypeService consultationTypeService;
    private final SpecialityCategoryService specialityCategoryService;

    @Override
    public void run(String... args) throws Exception {
        categoryService.seeder();
        specialityService.seeder();
        consultationTypeService.seeder();
        doctorService.seeder();
        specialityCategoryService.seeder();
    }
}
