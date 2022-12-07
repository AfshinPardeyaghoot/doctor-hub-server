package com.project.doctorhub.auth.repository;

import com.project.doctorhub.auth.model.RefreshToken;
import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.user.model.User;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository
        extends AbstractRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUUIDAndExpireAtIsAfterAndIsDeletedFalse(String uuid, Instant now);

}
