package com.project.doctorhub.auth.config.security.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doctorhub.auth.dto.UserCredentials;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.base.dto.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter
        extends OncePerRequestFilter {

    public static final String BEARER_KEYWORD = "Bearer ";
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        if (request.getServletPath().equals("/api/v1/auth/login") || request.getServletPath().equals("/api/v1/auth/sendVerificationCode") || request.getServletPath().startsWith("/ws")) {
            filterChain.doFilter(request, response);

        } else {

            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_KEYWORD)) {
                String token = authorizationHeader.substring(BEARER_KEYWORD.length());
                try {
                    UserCredentials userCredentials = jwtUtil.extractUserCredentials(token);
                    SecurityContextHolder.getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            userCredentials.getUuid(),
                                            null,
                                            userCredentials.getAuthorities()
                                    )
                            );

                    filterChain.doFilter(request, response);

                } catch (SignatureVerificationException ex) {
                    handleSignatureVerificationException(response);
                } catch (TokenExpiredException ex) {
                    handleTokenExpiredException(response);
                }

            } else {
                filterChain.doFilter(request, response);
            }

        }
    }

    private void handleSignatureVerificationException(HttpServletResponse response) throws IOException {
        response.setStatus(400);
        HttpResponse<?> httpResponse = new HttpResponse<>(
                new HttpResponseStatus(
                        "فرمن توکن ارسالی صحیح نمی باشد!",
                        HttpStatus.BAD_REQUEST.value()
                )
        );
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), httpResponse);
    }

    private void handleTokenExpiredException(HttpServletResponse response) throws IOException {
        response.setStatus(403);
        HttpResponse<?> httpResponse = new HttpResponse<>(
                new HttpResponseStatus(
                        "توکن ارسالی منقضی شده است!",
                        403
                )
        );
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), httpResponse);
    }

}
