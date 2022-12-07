package com.project.doctorhub.doctor.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.speciality.model.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DoctorRepository
        extends AbstractRepository<Doctor, Long> {

    @Query("select d from Doctor d " +
            "inner join User u on d.user = u " +
            "where u.phone = :phone " +
            "and d.isDeleted = false")
    Optional<Doctor> findByPhone(String phone);

    @Query("select d from Doctor d " +
            "inner join User u on d.user = u " +
            "where ( u.firstName like '%:name%' or u.lastName like '%:name%' ) " +
            "and d.isDeleted = false ")
    Page<Doctor> findAllByNameLike(String name, Pageable pageable);

}
