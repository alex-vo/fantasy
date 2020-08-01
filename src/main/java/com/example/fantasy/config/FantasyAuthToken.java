package com.example.fantasy.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class FantasyAuthToken extends PreAuthenticatedAuthenticationToken {
    public FantasyAuthToken(FantasyPrincipal principal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(principal, aCredentials, anAuthorities);
    }

    public Long getUserId() {
        return ((FantasyPrincipal) getPrincipal()).getId();
    }

    public String getEmail() {
        return ((FantasyPrincipal) getPrincipal()).getName();
    }
}
