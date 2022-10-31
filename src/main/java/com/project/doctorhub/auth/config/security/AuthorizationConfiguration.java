package com.project.doctorhub.auth.config.security;

import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration
        extends WebSecurityConfigurerAdapter {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    public AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("AuthenticationManager can not bean", e);
        }
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OtpAuthenticationFilter otpAuthenticationFilter =
                new OtpAuthenticationFilter(
                        jwtUtil,
                        userService,
                        authenticationManager()
                );
        otpAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/auth/login", "/api/v1/auth/sendVerificationCode").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(otpAuthenticationFilter);
    }
}
