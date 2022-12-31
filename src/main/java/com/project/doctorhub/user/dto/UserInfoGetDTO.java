package com.project.doctorhub.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoGetDTO {

    private String id;
    private String phone;
    private String username;
    private List<String> roles;

}
