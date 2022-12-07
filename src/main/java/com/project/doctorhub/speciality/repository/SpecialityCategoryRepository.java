package com.project.doctorhub.speciality.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.model.SpecialityCategory;

import java.util.List;
import java.util.Optional;

public interface SpecialityCategoryRepository
        extends AbstractRepository<SpecialityCategory, Long> {

    Optional<SpecialityCategory> findByCategoryAndSpeciality(Category category, Speciality speciality);

    List<SpecialityCategory> findAllByCategoryAndIsDeletedFalse(Category category);

}
