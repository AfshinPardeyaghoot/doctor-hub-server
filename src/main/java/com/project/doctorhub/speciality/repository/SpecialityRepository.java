package com.project.doctorhub.speciality.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.speciality.model.Speciality;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository
        extends AbstractRepository<Speciality, Long> {

    Optional<Speciality> findByNameIgnoreCase(String name);
    List<Speciality> findAllByTitleContainingIgnoreCaseAndIsDeletedFalse(String title);
}
