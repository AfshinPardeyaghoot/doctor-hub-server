package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.RefreshToken;
import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.repository.RefreshTokenRepository;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService
        extends AbstractCrudService<RefreshToken, Long, RefreshTokenRepository> {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository abstractRepository) {
        super(abstractRepository);
        this.refreshTokenRepository = abstractRepository;
    }


    public RefreshToken generateRefreshToken(User user, Integer lifeTime) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElseGet(() -> create(user));

        refreshToken.setUUID(UUID.randomUUID().toString());
        refreshToken.setExpireAt(Instant.now().plus(lifeTime, ChronoUnit.MINUTES));
        return save(refreshToken);
    }

    private RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setIsDeleted(false);
        return refreshToken;
    }


}
