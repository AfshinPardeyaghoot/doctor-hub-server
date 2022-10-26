package com.project.doctorhub.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ApplicationUserDetails implements UserDetails {

    private final com.project.doctorhub.auth.model.User applicationUser;

    public ApplicationUserDetails(com.project.doctorhub.auth.model.User applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return applicationUser.getRoles();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return applicationUser.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
