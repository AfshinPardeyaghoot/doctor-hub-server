package com.project.doctorhub.doctor.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorConsultationType;

import java.util.Optional;

public interface DoctorConsultationTypeRepository
        extends AbstractRepository<DoctorConsultationType, Long> {


    Optional<DoctorConsultationType> findByDoctorAndConsultationType(Doctor doctor, ConsultationType consultationType);
}
