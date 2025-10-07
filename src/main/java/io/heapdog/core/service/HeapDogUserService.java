package io.heapdog.core.service;


import io.heapdog.core.UserMapper;
import io.heapdog.core.dto.SignupRequestDto;
import io.heapdog.core.dto.SignupResponseDto;
import io.heapdog.core.exception.DuplicateUsernameException;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.repository.HeapDogUserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class HeapDogUserService {

    private final HeapDogUserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public SignupResponseDto createUser(@Valid SignupRequestDto dto) {
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
