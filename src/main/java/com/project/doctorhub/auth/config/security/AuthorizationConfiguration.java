package com.project.doctorhub.auth.config.security;

import com.project.doctorhub.auth.config.security.filter.JwtAuthorizationFilter;
import com.project.doctorhub.auth.config.security.filter.OtpAuthenticationFilter;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.user.dto.UserDTOMapper;
import com.project.doctorhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration
        extends WebSecurityConfigurerAdapter {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("AuthenticationManager can not bean", e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OtpAuthenticationFilter otpAuthenticationFilter = new OtpAuthenticationFilter(jwtUtil, userService, userDTOMapper, authenticationManager());
        otpAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests().antMatchers("/ws/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/sendVerificationCode").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/auth/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/file/download/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/chat/**").authenticated();
        http.authorizeRequests().antMatchers("/api/v1/user/admin", "/api/v1/user/**/admin", "/api/v1/user/**/info/admin", "/api/v1/auth/role").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/speciality").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/speciality/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/chat/**/end").hasAuthority("DOCTOR");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/doctor/**", "/api/v1/schedule/doctor/**", "/api/v1/category/**", "/api/v1/speciality/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/doctor/**", "/api/v1/speciality/**", "/api/v1/schedule/doctor/**", "/api/v1/category/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/doctor/**").hasAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.addFilter(otpAuthenticationFilter);
    }
}
