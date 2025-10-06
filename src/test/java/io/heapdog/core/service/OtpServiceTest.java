package io.heapdog.core.service;

import io.heapdog.core.repository.PasswordResetOtpRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.model.PasswordResetOtp;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private PasswordResetOtpRepository otpRepository;

    @InjectMocks
    private OtpService otpService;

    @Test
    void generateOtp_shouldCreateAndSaveOtp() {
        HeapDogUser testUser = new HeapDogUser();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        ArgumentCaptor<PasswordResetOtp> otpCaptor = ArgumentCaptor.forClass(PasswordResetOtp.class);

        String generatedOtp = otpService.generateOtp(testUser);

        assertThat(generatedOtp).isNotNull();
        assertThat(generatedOtp.length()).isEqualTo(6);
        assertThat(generatedOtp).matches("\\d{6}"); // Asserts it's 6 digits

        verify(otpRepository).deleteByUser(testUser);

        verify(otpRepository).save(otpCaptor.capture());

        PasswordResetOtp savedOtp = otpCaptor.getValue();

        assertThat(savedOtp.getUser()).isEqualTo(testUser);
        assertThat(savedOtp.getOtp()).isEqualTo(generatedOtp);
        assertThat(savedOtp.getExpiresAt()).isAfter(LocalDateTime.now().plusMinutes(9));
        assertThat(savedOtp.getExpiresAt()).isBefore(LocalDateTime.now().plusMinutes(11));
    }
}