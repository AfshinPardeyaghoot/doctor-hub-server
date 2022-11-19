package com.project.doctorhub.auth.model;


import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity<Long> {

    @Column(name = "expire_at", nullable = false)
    private Instant expireAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

}
