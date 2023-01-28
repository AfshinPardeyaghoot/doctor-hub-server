package com.project.doctorhub.speciality.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.speciality.model.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository
        extends AbstractRepository<Speciality, Long> {

    Optional<Speciality> findByNameIgnoreCase(String name);

    List<Speciality> findAllByUUIDInAndIsDeletedFalse(List<String> uuids);


    @Query("select s from Speciality s where ( upper(s.name) like %:search% or upper(s.title) like %:search% ) and s.isDeleted = false ")
    Page<Speciality> findAllBySearchNotDeleted(String search, Pageable pageable);

}
