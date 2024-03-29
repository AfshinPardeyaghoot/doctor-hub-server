package com.project.doctorhub.category.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.doctor.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends AbstractRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    @Query("select d from Category c " +
            "inner join SpecialityCategory sc on sc.category = c " +
            "inner join Speciality s on s = sc.speciality " +
            "inner join Doctor d on d.speciality = s " +
            "and s.isDeleted = false " +
            "and d.isDeleted = false " +
            "and sc.isDeleted = false " +
            "and c.UUID = :uuid ")
    List<Doctor> findAllCategoryDoctors(String uuid);


    @Query("select c from Category c " +
            "where ( upper(c.title) like %:search% or upper(c.fullTitle) like %:search% or upper(c.name) like %:search% ) " +
            "and c.isDeleted = false ")
    Page<Category> findAllBySearchAndIsDeletedFalse(String search, Pageable pageable);
}
