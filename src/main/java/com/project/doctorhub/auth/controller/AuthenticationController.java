package com.project.doctorhub.auth.controller;

import com.project.doctorhub.auth.dto.SendVerificationCodeDTO;
import com.project.doctorhub.auth.service.UserService;
import com.project.doctorhub.base.dto.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
