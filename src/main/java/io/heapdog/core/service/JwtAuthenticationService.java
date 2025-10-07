package io.heapdog.core.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import io.heapdog.core.dto.SigninRequestDto;
import io.heapdog.core.dto.SigninResponseDto;
import io.heapdog.core.exception.JwtValidationFailedException;
import io.heapdog.core.security.jwt.JWTUtils;
import io.heapdog.core.security.jwt.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public SigninResponseDto authenticate(SigninRequestDto dto) throws JOSEException {
        Authentication unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(dto.getUsername(), dto.getPassword());
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(authenticated.getName())
                .expirationTime(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60)))
                .build();
        return SigninResponseDto
                .builder()
                .token(jwtUtils.generateToken(claimsSet))
                .build();
    }

    public JwtAuthenticationToken verifyToken(String token) throws JwtValidationFailedException {
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(token);
        return (JwtAuthenticationToken) authenticationManager.authenticate(unauthenticated);
    }
}
