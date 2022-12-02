package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.repository.SpecialityRepository;

public class SpecialityService
        extends AbstractCrudService<Speciality, Long, SpecialityRepository> {

    public SpecialityService(SpecialityRepository abstractRepository) {
        super(abstractRepository);
    }
}
