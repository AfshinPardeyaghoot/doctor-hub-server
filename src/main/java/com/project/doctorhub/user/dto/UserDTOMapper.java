package com.project.doctorhub.user.dto;

import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.dto.UserLoginResponseDTO;
import com.project.doctorhub.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    public UserInfoGetDTO entityToInfoDTO(User user) {
        UserInfoGetDTO dto = new UserInfoGetDTO();
        dto.setId(user.getUUID());
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles().stream().map(Object::toString).collect(Collectors.toList()));
        return dto;
    }

    public UserInfoFullDTO entityToFullDTO(User user) {
        UserInfoFullDTO dto = new UserInfoFullDTO();
        dto.setId(user.getUUID());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        if (user.getRoles().stream().anyMatch(role -> Objects.equals(role.getAuthority(), "ADMIN")))
            dto.setRole("ADMIN");
        else if (user.getRoles().stream().anyMatch(role -> Objects.equals(role.getAuthority(), "INVALID")))
            dto.setRole("INVALID");
        else if (user.getRoles().stream().anyMatch(role -> Objects.equals(role.getAuthority(), "DOCTOR")))
            dto.setRole("DOCTOR");
        else dto.setRole("USER");
        return dto;
    }

    public UserLoginResponseDTO entityToUserLoginResponseDTO(
            User user,
            AuthenticationTokenDTO authenticationTokenDTO
    ) {
        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
        userLoginResponseDTO.setId(user.getUUID());
        userLoginResponseDTO.setFirstName(user.getFirstName());
        userLoginResponseDTO.setLastName(user.getLastName());
        userLoginResponseDTO.setRoles(
                user.getRoles().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(Object::toString)
                        .collect(Collectors.toSet())
        );
        userLoginResponseDTO.setToken(authenticationTokenDTO);
        return userLoginResponseDTO;
    }


}
