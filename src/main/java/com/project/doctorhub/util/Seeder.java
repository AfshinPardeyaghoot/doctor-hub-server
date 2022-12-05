package com.project.doctorhub.util;

import com.project.doctorhub.speciality.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final SpecialityService specialityService;

    @Override
    public void run(String... args) throws Exception {
        specialityService.seeder();
    }
}
