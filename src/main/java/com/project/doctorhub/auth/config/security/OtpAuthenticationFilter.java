package com.project.doctorhub.auth.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.service.UserService;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.base.dto.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RequiredArgsConstructor
public class OtpAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("in authenticate filter user Authentication object created");
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        log.info("Phone is {} and Code is {}", phone, code);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                phone,
                code
        );

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String phone = (String) authResult.getPrincipal();
        User user = userService.findByPhone(phone);
        AuthenticationTokenDTO authenticationTokenDTO = jwtUtil.generateAuthenticationToken(user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new HttpResponse<>(authenticationTokenDTO));
    }
}
