package com.project.doctorhub.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {

    private String id;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private AuthenticationTokenDTO token;
}
