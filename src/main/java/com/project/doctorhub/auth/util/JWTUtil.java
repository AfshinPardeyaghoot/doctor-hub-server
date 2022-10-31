package com.project.doctorhub.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.doctorhub.auth.config.jwt.JWTProperties;
import com.project.doctorhub.auth.dto.AccessTokenDTO;
import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.dto.UserCredentials;
import com.project.doctorhub.auth.model.RefreshToken;
import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final JWTProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;


    public AuthenticationTokenDTO generateAuthenticationToken(User user) {
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user, jwtProperties.getRefreshTokenLifeTime());
        AccessTokenDTO accessTokenDTO = generateAccessToken(user);

        return new AuthenticationTokenDTO(
                accessTokenDTO.getToken(),
                refreshToken.getUUID(),
                accessTokenDTO.getExpireAt().toEpochMilli(),
                refreshToken.getExpireAt().toEpochMilli()
        );

    }

    public UserCredentials extractUserCredentials(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        String uuid = decodedJWT.getSubject();
        Set<SimpleGrantedAuthority> authorities = Arrays
                .stream(decodedJWT.getClaim("roles").asArray(String.class))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UserCredentials(uuid, authorities);
    }

    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private AccessTokenDTO generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes());
        Instant accessTokenExpireAt = Instant.now().plus(jwtProperties.getAccessTokenLifeTime(), ChronoUnit.MINUTES);
        String accessToken = JWT.create()
                .withSubject(user.getUUID())
                .withExpiresAt(Date.from(accessTokenExpireAt))
                .withClaim("roles", user.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        return new AccessTokenDTO(accessToken, accessTokenExpireAt);
    }


}
