package com.example.exercise.demo.security;

import com.example.exercise.demo.entity.UsersEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UsersEntity user;

    public CustomUserDetails(UsersEntity user) {
        this.user = user;
    }

    /** user email auth */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /** password */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /** user id */
    public Integer getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted();
    }


}
