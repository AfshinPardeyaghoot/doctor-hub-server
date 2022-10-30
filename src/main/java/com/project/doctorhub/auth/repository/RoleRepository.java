package com.project.doctorhub.auth.repository;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.base.repository.AbstractRepository;

import java.util.Optional;

public interface RoleRepository extends AbstractRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);
}
