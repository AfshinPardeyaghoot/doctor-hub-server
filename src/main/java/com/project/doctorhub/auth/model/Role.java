package com.project.doctorhub.auth.model;

import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role extends BaseEntity<Long> {

    @Transient
    public static final String USER_ROLE = "USER";

    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();

}
