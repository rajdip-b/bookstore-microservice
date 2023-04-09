package com.bookstorems.userservice.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String email;
    private final String role;
    private final String userId;

    public CustomAuthenticationToken(String email, String role, String userId) {
        super(email, null, List.of(new SimpleGrantedAuthority(role)));
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
}
