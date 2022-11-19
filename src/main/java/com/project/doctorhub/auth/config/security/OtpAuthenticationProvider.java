package com.project.doctorhub.auth.config.security;

import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String phone = (String) authentication.getPrincipal();
        String code = (String) authentication.getCredentials();
        log.info("Authentication provider phone is {} and code is {}", phone, code);

        User user = userService.findByPhone(phone);

        if (Objects.equals(user.getAuthenticationCode(), code)
                && user.getAuthenticationCodeExpAt() != null
                && user.getAuthenticationCodeExpAt().isAfter(Instant.now()))
            return authentication;
        else throw new BadCredentialsException("کد وارد شده معتبر نمی باشد!");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
