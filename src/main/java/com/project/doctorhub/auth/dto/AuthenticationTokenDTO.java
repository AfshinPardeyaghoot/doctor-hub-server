package com.project.doctorhub.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationTokenDTO {
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpireAt;
    private long refreshTokenExpireAt;
}
