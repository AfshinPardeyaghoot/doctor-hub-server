package com.project.doctorhub.user.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.user.dto.UserDTOMapper;
import com.project.doctorhub.user.dto.UserInfoFullDTO;
import com.project.doctorhub.user.dto.UserInfoGetDTO;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    @GetMapping("/info")
    public ResponseEntity<HttpResponse<UserInfoGetDTO>> getUserInfo(Authentication authentication) {
        String userUUID = (String) authentication.getPrincipal();
        User user = userService.findByUUID(userUUID);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToInfoDTO(user)));
    }

    @GetMapping
    public ResponseEntity<HttpResponse<UserInfoFullDTO>> getUserFullInfo(Authentication authentication){
        User user = userService.findByAuthentication(authentication);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToFullDTO(user)));
    }
}
