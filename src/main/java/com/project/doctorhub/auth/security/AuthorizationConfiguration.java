package com.project.doctorhub.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {

    public AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("AuthenticationManager can not bean", e);
        }
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OtpAuthenticationFilter otpAuthenticationFilter = new OtpAuthenticationFilter(authenticationManager());
        otpAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/login").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(otpAuthenticationFilter);
    }
}
