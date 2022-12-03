package com.project.doctorhub.doctor.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorSpeciality;
import com.project.doctorhub.speciality.model.Speciality;

import java.util.Optional;

public interface DoctorSpecialityRepository
        extends AbstractRepository<DoctorSpeciality, Long> {


    Optional<DoctorSpeciality> findByDoctorAndSpeciality(Doctor doctor, Speciality speciality);
}
