package io.heapdog.core.service;


import io.heapdog.core.UserMapper;
import io.heapdog.core.dto.SigninRequestDto;
import io.heapdog.core.dto.SigninResponseDto;
import io.heapdog.core.dto.SignupRequestDto;
import io.heapdog.core.dto.SignupResponseDto;
import io.heapdog.core.exception.DuplicateUsernameException;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.repository.HeapDogUserRepository;
import io.heapdog.core.security.jwt.JWTUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class HeapDogUserService {

    private final HeapDogUserRepository repository;
    private final AuthenticationManager manager;
    private final JWTUtils jwtUtils;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public SigninResponseDto authenticate(SigninRequestDto dto) throws JOSEException {
        Authentication unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(dto.getUsername(), dto.getPassword());
        Authentication authenticated = manager.authenticate(unauthenticated);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(authenticated.getName())
                .expirationTime(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60)))
                .build();
        return SigninResponseDto
                .builder()
                .token(jwtUtils.generateToken(claimsSet))
                .build();
    }

    public SignupResponseDto createUser(SignupRequestDto dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        HeapDogUser user = mapper.toEntity(dto);
        user.setRole(Set.of(HeapDogUser.Role.ROLE_USER));
        try {
            HeapDogUser saved = repository.save(user);
            return mapper.toDto(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateUsernameException("Username already exists: " + dto.getUsername());
        }
    }
}
