package com.project.doctorhub.auth.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JWTProperties {

    private final String secretKey;
    private final Integer accessTokenLifeTime;
    private final Integer refreshTokenLifeTime;

    public JWTProperties(
            @Value("${doctorhub.auth.jwt.secretKey}") String secretKey,
            @Value("${doctorhub.auth.jwt.accessToken.lifeTime}") Integer accessTokenLifeTime,
            @Value("${doctorhub.auth.jwt.refreshToken.lifeTime}") Integer refreshTokenLifeTime
    ) {
        this.secretKey = secretKey;
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
    }


}
