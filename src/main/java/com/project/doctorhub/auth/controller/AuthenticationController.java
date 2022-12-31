package com.project.doctorhub.auth.controller;

import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.dto.SendVerificationCodeDTO;
import com.project.doctorhub.auth.dto.UserLoginResponseDTO;
import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.user.dto.UserDTOMapper;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<HttpResponse<Map<String, Object>>> sendVerificationCode(
            @Valid @RequestBody SendVerificationCodeDTO sendVerificationCodeDTO,
            HttpServletResponse response
    ) {
        userService.sendUserAuthenticationCode(sendVerificationCodeDTO.getPhone());
        return ResponseEntity.ok(new HttpResponse<>(Map.of("message", "کد احراز هویت برای شما ارسال شد.")));
    }

    @GetMapping("/token/refresh/{uuid}")
    public ResponseEntity<HttpResponse<UserLoginResponseDTO>> getAccessTokenByRefreshToken(
            @PathVariable String uuid,
            Authentication authentication
    ) {
        User user = userService.findByAuthentication(authentication);
        AuthenticationTokenDTO authenticationTokenDTO = userService.refreshTokens(uuid);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToUserLoginResponseDTO(user, authenticationTokenDTO)));
    }
}
