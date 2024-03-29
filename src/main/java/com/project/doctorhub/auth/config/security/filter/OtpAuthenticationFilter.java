package com.project.doctorhub.auth.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doctorhub.auth.dto.AuthenticationRequestDTO;
import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.base.dto.HttpResponseStatus;
import com.project.doctorhub.user.dto.UserDTOMapper;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
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
    private final UserDTOMapper userDTOMapper;
    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            AuthenticationRequestDTO authenticationRequestDTO =
                    new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequestDTO.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getPhone(),
                    authenticationRequestDTO.getCode()
            );

            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String phone = (String) authResult.getPrincipal();
        User user = userService.findByPhone(phone);
        AuthenticationTokenDTO authenticationTokenDTO = jwtUtil.generateAuthenticationToken(user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new HttpResponse<>(userDTOMapper.entityToUserLoginResponseDTO(user, authenticationTokenDTO)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(400);
        HttpResponse<?> httpResponse = new HttpResponse<>(
                new HttpResponseStatus(
                        "کد وارد شده معتبر نمی باشد!",
                        400
                )
        );
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), httpResponse);
    }


}
