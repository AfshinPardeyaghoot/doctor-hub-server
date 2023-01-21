package com.project.doctorhub.user.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.user.dto.UserDTOMapper;
import com.project.doctorhub.user.dto.UserInfoFullDTO;
import com.project.doctorhub.user.dto.UserInfoGetDTO;
import com.project.doctorhub.user.dto.UserUpdateInfoDTO;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    @GetMapping("/info")
    public ResponseEntity<HttpResponse<UserInfoGetDTO>> getUserInfo(
            Authentication authentication
    ) {
        String userUUID = (String) authentication.getPrincipal();
        User user = userService.findByUUID(userUUID);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToInfoDTO(user)));
    }

    @GetMapping
    public ResponseEntity<HttpResponse<UserInfoFullDTO>> getUserFullInfo(
            Authentication authentication
    ) {
        User user = userService.findByAuthentication(authentication);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToFullDTO(user)));
    }

    @PutMapping("/info")
    public ResponseEntity<HttpResponse<UserInfoFullDTO>> updateUserInfo(
            Authentication authentication,
            @RequestBody UserUpdateInfoDTO userUpdateInfoDTO
    ) {
        User user = userService.findByAuthentication(authentication);
        userService.update(user, userUpdateInfoDTO);
        return ResponseEntity.ok(new HttpResponse<>(userDTOMapper.entityToFullDTO(user)));
    }

    @GetMapping("/admin")
    public ResponseEntity<HttpResponse<Page<UserInfoGetDTO>>> getAllUsers(
            @RequestParam(required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        Page<User> users = userService.findAllByPhoneOrNameNotDeleted(search, pageable);
        return ResponseEntity.ok(new HttpResponse<>(users.map(userDTOMapper::entityToInfoDTO)));
    }
}
