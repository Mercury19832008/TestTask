package ru.fors.sample.core.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Maxim Kropachev
 * 27.05.2016
 */
public class SampleAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private Long id;

    public SampleAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities, Long id) {
        super(principal, credentials, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
