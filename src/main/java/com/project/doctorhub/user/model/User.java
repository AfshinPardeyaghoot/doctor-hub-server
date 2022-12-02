package com.project.doctorhub.user.model;

import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity<Long> {


    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "authentication_code")
    private String authenticationCode;

    @Column(name = "authentication_code_exp_at")
    private Instant authenticationCodeExpAt;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();
    public String getUsername() {
        return String.format("%s %s", firstName, lastName);
    }

    public Set<GrantedAuthority> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

}
