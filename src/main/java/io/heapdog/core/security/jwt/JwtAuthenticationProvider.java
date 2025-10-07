package io.heapdog.core.security.jwt;

import io.heapdog.core.exception.JwtValidationFailedException;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.repository.HeapDogUserRepository;
import io.heapdog.core.security.SecurityUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@AllArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JWTUtils jwtUtils;
    private final HeapDogUserRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        try {
            JWTClaimsSet claimsSet = jwtUtils.verify(token);
            HeapDogUser user = repository.findByUsername(claimsSet.getSubject())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            SecurityUser securityUser = new SecurityUser(user);
            return JwtAuthenticationToken.authenticated(securityUser, securityUser.getAuthorities());
        } catch (ParseException | JOSEException | JwtValidationFailedException e) {
            throw new JwtValidationFailedException("Invalid token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
