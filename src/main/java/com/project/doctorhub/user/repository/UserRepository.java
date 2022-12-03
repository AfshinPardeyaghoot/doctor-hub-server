package com.project.doctorhub.user.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.user.model.User;

import java.util.Optional;

public interface UserRepository
        extends AbstractRepository<User, Long> {

    Optional<User> findByPhone(String phone);
}
