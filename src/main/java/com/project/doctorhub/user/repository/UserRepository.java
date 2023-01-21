package com.project.doctorhub.user.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository
        extends AbstractRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    @Query("select u from User u " +
            "where (upper(u.firstName) like %:search% " +
            "or upper(u.lastName) like %:search% " +
            "or u.phone like %:search% )" +
            "and u.isDeleted = false ")
    Page<User> findAllByNameOrPhoneLikeNotDeleted(String search, Pageable pageable);
}
