package com.project.doctorhub.auth.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_role")
public class UserRole extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
