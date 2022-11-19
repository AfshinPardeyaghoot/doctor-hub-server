package com.project.doctorhub.auth.controller;

import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.dto.SendVerificationCodeDTO;
import com.project.doctorhub.user.service.UserService;
import com.project.doctorhub.base.dto.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<HttpResponse<Map<String, Object>>> sendVerificationCode(
            @Valid @RequestBody SendVerificationCodeDTO sendVerificationCodeDTO,
            HttpServletResponse response
    ) {
        userService.sendUserAuthenticationCode(sendVerificationCodeDTO.getPhone());
        return ResponseEntity.ok(new HttpResponse<>(Map.of("message", "کد احراز هویت برای شما ارسال شد.")));
    }

    @GetMapping("/token/refresh/{uuid}")
    public ResponseEntity<HttpResponse<AuthenticationTokenDTO>> getAccessTokenByRefreshToken(
            @PathVariable String uuid
    ) {
        AuthenticationTokenDTO authenticationTokenDTO = userService.refreshTokens(uuid);
        return ResponseEntity.ok(new HttpResponse<>(authenticationTokenDTO));
    }
}
