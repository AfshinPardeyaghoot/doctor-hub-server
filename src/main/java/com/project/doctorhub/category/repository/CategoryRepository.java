package com.project.doctorhub.category.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.category.model.Category;

import java.util.Optional;

public interface CategoryRepository extends AbstractRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

}
