package com.project.doctorhub.consultation.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.consultation.model.ConsultationRate;
import com.project.doctorhub.doctor.model.Doctor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultationRateRepository
        extends AbstractRepository<ConsultationRate, Long> {



    @Query("select cr.rate from ConsultationRate cr inner join Consultation c on cr.consultation = c inner join Doctor d on c.doctor = d where d = :doctor")
    List<Integer> findAllByDoctor(Doctor doctor);

}
