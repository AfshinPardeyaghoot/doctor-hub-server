package com.project.doctorhub.auth.repository;

import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.user.model.User;

import java.util.List;

public interface UserRoleRepository
        extends AbstractRepository<UserRole, Long> {

    List<UserRole> findAllByUser(User user);
}
