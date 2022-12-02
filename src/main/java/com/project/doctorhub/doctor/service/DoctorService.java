package com.project.doctorhub.doctor.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService
        extends AbstractCrudService<Doctor, Long, DoctorRepository> {

    public DoctorService(DoctorRepository abstractRepository) {
        super(abstractRepository);
    }



}
