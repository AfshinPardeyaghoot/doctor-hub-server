package com.project.doctorhub.auth.repository;

import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.base.repository.AbstractRepository;

import java.util.Optional;

public interface UserRepository
        extends AbstractRepository<User, Long> {

    Optional<User> findByPhone(String phone);
}
