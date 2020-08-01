package com.example.fantasy.config;

import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class FantasyPrincipal implements Principal {

    private final Long id;
    private final String email;

    @Override
    public String getName() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
