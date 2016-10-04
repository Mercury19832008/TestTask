package ru.fors.sample.web.security;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.fors.sample.core.dto.User;

import java.util.stream.Collectors;

public class SampleSession extends AbstractAuthenticatedWebSession {
    private static final long serialVersionUID = 5933977007999803447L;

    private User user;

    public SampleSession(Request request) {
        super(request);
    }

    public static SampleSession get() {
        return (SampleSession) Session.get();
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();

        if (isSignedIn()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                roles.addAll(authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                );
            }
        }
        return roles;
    }


    @Override
    public boolean isSignedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}