package com.project.doctorhub.category.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.doctor.model.Doctor;
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
}
