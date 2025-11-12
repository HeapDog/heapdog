package io.heapdog.core.feature.auth;

import io.heapdog.core.feature.user.HeapDogUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class OtpService {

    private final PasswordResetOtpRepository otpRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp(HeapDogUser user){
        otpRepository.deleteByUser(user);

        String otpString = String.format("%06d", secureRandom.nextInt(1000000));

        PasswordResetOtp otp =  PasswordResetOtp.builder()
                .user(user)
                .otp(otpString)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        otpRepository.save(otp);

        return  otpString;
    }
}
