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

    @Query("select d from Doctor d " +
            "inner join DoctorSpeciality ds on ds.doctor = d " +
            "inner join User u on d.user = u " +
            "where ds.speciality = :speciality " +
            "and (u.firstName like '%:name%' or u.lastName like '%:name%') " +
            "and d.isDeleted = false " +
            "and ds.isDeleted = false ")
    Page<Doctor> findAllBySpecialityAndNameLike(Speciality speciality, String name, Pageable pageable);

    @Query("select d from Doctor d " +
            "inner join DoctorSpeciality ds on ds.doctor = d " +
            "where ds.isDeleted = false " +
            "and d.isDeleted = false " +
            "and ds.speciality = :speciality ")
    Page<Doctor> findAllBySpeciality(Speciality speciality, Pageable pageable);

}
