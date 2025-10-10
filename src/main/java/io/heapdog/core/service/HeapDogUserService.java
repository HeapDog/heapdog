package io.heapdog.core.service;


import io.heapdog.core.UserMapper;
import io.heapdog.core.dto.*;
import io.heapdog.core.exception.DuplicateUsernameException;
import io.heapdog.core.exception.InvalidOtpException;
import io.heapdog.core.exception.UserNotFoundException;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.model.PasswordResetOtp;
import io.heapdog.core.repository.HeapDogUserRepository;
import io.heapdog.core.repository.PasswordResetOtpRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class HeapDogUserService {

    private final HeapDogUserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final PasswordResetOtpRepository otpRepository;
    private final OtpService otpService;

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

    public PasswordResetResponseDto generatePasswordResetOtp(PasswordResetRequestDto dto) {
        // Find the user by email.
        HeapDogUser user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        String otpString = otpService.generateOtp(user);

        log.info("MOCK EMAIL: OTP for user {} is: {}", user.getEmail(), otpString);

        return PasswordResetResponseDto.builder()
                .message("OTP sent successfully (mock).")
                .build();
    }

    public PasswordResetResponseDto verifyOtpAndResetPassword(PasswordResetVerifyRequestDto dto) {
        HeapDogUser user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        PasswordResetOtp otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new InvalidOtpException("No OTP found for this user. Please request a new one."));

        if (!otp.getOtp().equals(dto.getOtp()) || LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new InvalidOtpException("The OTP provided is incorrect or has expired.");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        repository.save(user);

        otpRepository.delete(otp);

        return PasswordResetResponseDto.builder()
                .message("Password reset successful.")
                .build();
    }

}
