package com.project.doctorhub.util;

import com.project.doctorhub.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    private final CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        categoryService.seeder();
    }
}
