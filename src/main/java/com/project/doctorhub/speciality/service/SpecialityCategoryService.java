package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.category.service.CategoryService;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.model.SpecialityCategory;
import com.project.doctorhub.speciality.repository.SpecialityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialityCategoryService
        extends AbstractCrudService<SpecialityCategory, Long, SpecialityCategoryRepository> {

    private final CategoryService categoryService;
    private final SpecialityService specialityService;
    private final SpecialityCategoryRepository specialityCategoryRepository;

    public SpecialityCategoryService(
            CategoryService categoryService,
            SpecialityCategoryRepository abstractRepository,
            SpecialityService specialityService
    ) {
        super(abstractRepository);
        this.categoryService = categoryService;
        this.specialityService = specialityService;
        this.specialityCategoryRepository = abstractRepository;
    }


    public List<SpecialityCategory> createAll(Category category, List<String> specialityIds) {
        List<Speciality> specialities = specialityService.findAllByUUIDs(specialityIds);
        List<SpecialityCategory> specialityCategories = new ArrayList<>();

        for (Speciality speciality : specialities) {
            Optional<SpecialityCategory> specialityCategoryOptional = specialityCategoryRepository.findByCategoryAndSpeciality(category, speciality);
            SpecialityCategory specialityCategory;
            if (specialityCategoryOptional.isPresent()) {
                specialityCategory = specialityCategoryOptional.get();
            } else {
                specialityCategory = new SpecialityCategory();
                specialityCategory.setCategory(category);
                specialityCategory.setSpeciality(speciality);
            }
            specialityCategory.setIsDeleted(false);
            save(specialityCategory);
            specialityCategories.add(specialityCategory);
        }

        return specialityCategories;
    }

    public void updateCategorySpecialities(Category category, List<String> specialityIds) {
        deleteAllByCategory(category);
        createAll(category, specialityIds);
    }

    private void deleteAllByCategory(Category category) {
        List<SpecialityCategory> specialityCategories = findAllByCategoryNotDeleted(category);
        specialityCategories.forEach(this::safeDelete);
    }

    private List<SpecialityCategory> findAllByCategoryNotDeleted(Category category) {
        return specialityCategoryRepository.findAllByCategoryAndIsDeletedFalse(category);
    }


    public void seeder() {

        createIfNotExist("nutrition", "nutrition_senior");
        createIfNotExist("nutrition", "nutrition");

        createIfNotExist("child", "child_expert");
        createIfNotExist("child", "child_expert_assistant");
        createIfNotExist("child", "child_specialist");

        createIfNotExist("dental", "dentist");
        createIfNotExist("dental", "gum_surgery_expert");

        createIfNotExist("ophthalmology", "ophthalmology_expert");
        createIfNotExist("ophthalmology", "optometry_expert");
        createIfNotExist("ophthalmology", "ophthalmology_expert_assistant");

        createIfNotExist("general", "general");

        createIfNotExist("otorhinolaryngology", "otorhinolaryngology_expert");
        createIfNotExist("otorhinolaryngology", "ophthalmology_expert_assistant");

        createIfNotExist("cardiovascular", "cardiovascular_expert");
        createIfNotExist("cardiovascular", "cardiovascular_specialist");
        createIfNotExist("cardiovascular", "cardiovascular_expert_assistant");

        createIfNotExist("general_surgeon", "general_surgeon_expert");
        createIfNotExist("general_surgeon", "general_surgeon_expert_assistant");

        createIfNotExist("neurology", "neurology_expert");
        createIfNotExist("neurology", "neurology_specialist");
        createIfNotExist("neurology", "neurology_expert_assistant");
        createIfNotExist("neurology", "neurology_resident");

        createIfNotExist("orthopedist", "orthopedist_expert");
        createIfNotExist("orthopedist", "orthopedist_expert_assistant");
        createIfNotExist("orthopedist", "orthopedist_expert");
        createIfNotExist("orthopedist", "orthopedist_resident");

        createIfNotExist("plastic_surgery", "plastic_surgery_specialist");
        createIfNotExist("plastic_surgery", "plastic_surgery_expert_assistant");
        createIfNotExist("plastic_surgery", "plastic_surgery_fellowship");

        createIfNotExist("dermatologist", "dermatologist_expert_assistant");
        createIfNotExist("dermatologist", "dermatologist_expert");

        createIfNotExist("obstetricians", "obstetricians_expert");
        createIfNotExist("obstetricians", "obstetricians_expert_assistant");
    }

    private void createIfNotExist(String categoryName, String specialityName) {
        Category category = categoryService.findByName(categoryName);
        Speciality speciality = specialityService.findByName(specialityName);
        if (specialityCategoryRepository.findByCategoryAndSpeciality(category, speciality).isEmpty()) {
            SpecialityCategory specialityCategory = new SpecialityCategory();
            specialityCategory.setCategory(category);
            specialityCategory.setSpeciality(speciality);
            specialityCategory.setIsDeleted(false);
            save(specialityCategory);
        }
    }
}
