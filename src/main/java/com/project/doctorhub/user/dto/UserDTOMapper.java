package com.project.doctorhub.user.dto;

import com.project.doctorhub.user.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDTOMapper {

    public UserInfoGetDTO entityToInfoDTO(User user) {
        UserInfoGetDTO dto = new UserInfoGetDTO();
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles().stream().map(Object::toString).collect(Collectors.toList()));
        return dto;
    }
}
