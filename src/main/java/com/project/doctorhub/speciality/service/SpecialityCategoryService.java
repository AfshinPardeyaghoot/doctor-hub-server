package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.category.model.Category;
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

    private final SpecialityService specialityService;
    private final SpecialityCategoryRepository specialityCategoryRepository;

    public SpecialityCategoryService(
            SpecialityCategoryRepository abstractRepository,
            SpecialityService specialityService
    ) {
        super(abstractRepository);
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
}
