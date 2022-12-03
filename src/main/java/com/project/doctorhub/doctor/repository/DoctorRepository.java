package com.project.doctorhub.doctor.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.doctor.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository
        extends AbstractRepository<Doctor, Long> {

    @Query("select d from Doctor d inner join User u on d.user = u where u.phone = :phone and d.isDeleted = false")
    Optional<Doctor> findByPhone(String phone);
}
