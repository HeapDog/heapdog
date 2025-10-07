package io.heapdog.core.security.jwt;

import io.heapdog.core.security.SecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object token;

    public JwtAuthenticationToken(Object principal, Object token, Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(authenticated);
    }

    public static JwtAuthenticationToken authenticated(SecurityUser principal, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, null, authorities, true);
    }


    public static JwtAuthenticationToken unauthenticated(Object token) {
        return new JwtAuthenticationToken(null, token, null, false);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
