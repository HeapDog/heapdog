package io.heapdog.core.service;

import io.heapdog.core.dto.PasswordResetRequestDto;
import io.heapdog.core.dto.PasswordResetResponseDto;
import io.heapdog.core.dto.PasswordResetVerifyRequestDto;
import io.heapdog.core.exception.InvalidOtpException;
import io.heapdog.core.exception.UserNotFoundException;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.model.PasswordResetOtp;
import io.heapdog.core.repository.HeapDogUserRepository;
import io.heapdog.core.repository.PasswordResetOtpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Slf4j
@Service
@AllArgsConstructor
public class OtpService {

    private final PasswordResetOtpRepository otpRepository;
    private final HeapDogUserRepository userRepository;
    private final PasswordEncoder encoder;

    private static final SecureRandom secureRandom = new SecureRandom();

    static String generateOtp() {
        /*
            Generate a 6-digit OTP using SecureRandom.
            Here we have used a very simple approach to generate a 6-digit OTP.
            In a production system, you might want to consider additional factors such as:
            - Ensuring the OTP is unique for the user within a certain time frame.
            - Using a more complex algorithm to generate the OTP.
            - Adding rate limiting to prevent abuse.
            TODO: Implement these considerations as needed.
         */
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    public PasswordResetResponseDto generatePasswordResetOtp(PasswordResetRequestDto dto) {
        // Find the user by email.
        HeapDogUser user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        // Delete any existing OTPs for the user.
        if (otpRepository.findByUser(user).isPresent()) {
            otpRepository.deleteByUser(user);
        }

        // Generate the OTP.
        String otpString = generateOtp();

        // Create the OTP entity.
        PasswordResetOtp otp = PasswordResetOtp.builder()
                .user(user)
                .otp(otpString)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        // Log the OTP (mock email sending).
        log.info("MOCK EMAIL: OTP for user {} is: {}", user.getEmail(), otpString);

        // Save the OTP to the database.
        otpRepository.save(otp);

        // Return success response.
        return PasswordResetResponseDto.builder()
                .message("OTP sent successfully (mock).")
                .build();
    }


    public PasswordResetResponseDto verifyOtpAndResetPassword(PasswordResetVerifyRequestDto dto) {
        // Find the user by email.
        HeapDogUser user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        // Retrieve the OTP entity for the user.
        PasswordResetOtp otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new InvalidOtpException("No OTP found for this user. Please request a new one."));

        // Validate the OTP and check expiration.
        if (!otp.getOtp().equals(dto.getOtp()) || LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new InvalidOtpException("The OTP provided is incorrect or has expired.");
        }

        // Update the user's password.
        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // Delete the used OTP.
        otpRepository.delete(otp);

        // Return success response.
        return PasswordResetResponseDto.builder()
                .message("Password reset successful.")
                .build();
    }


}
