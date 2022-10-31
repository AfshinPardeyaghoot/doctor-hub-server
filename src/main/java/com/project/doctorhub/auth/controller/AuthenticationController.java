package com.project.doctorhub.auth.controller;

import com.project.doctorhub.auth.dto.SendVerificationCodeDTO;
import com.project.doctorhub.auth.service.UserService;
import com.project.doctorhub.base.dto.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(
            @Valid @RequestBody SendVerificationCodeDTO sendVerificationCodeDTO
    ) {
        userService.sendUserAuthenticationCode(sendVerificationCodeDTO.getPhone());
        return ResponseEntity.ok(new HttpResponse<>(Map.of("message", "کد احراز هویت برای شما ارسال شد.")));
    }

    @GetMapping("/token/refresh/{uuid}")
    public ResponseEntity<?> getAccessTokenByRefreshToken(@PathVariable String uuid) {
        return null;
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Hello " + authentication.getPrincipal());
    }
}
