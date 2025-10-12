package io.heapdog.core.service;

import io.heapdog.core.dto.PasswordResetRequestDto;
import io.heapdog.core.repository.HeapDogUserRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private PasswordResetOtpRepository otpRepository;

    @Mock
    private HeapDogUserRepository userRepository;

    @InjectMocks
    private OtpService otpService;

    @Test
    void generateOtp_thenReturns6DigitOtp() {
        String otp = OtpService.generateOtp();
        assertThat(otp).isNotNull();
        assertThat(otp.length()).isEqualTo(6);
        assertThat(otp).matches("\\d{6}"); // Asserts it's 6 digits
    }

    @Test
    void generatePasswordResetOtp_whenOtpNotExists_thenCreatesNewOtp() {
        // Create a test user
        HeapDogUser testUser = new HeapDogUser();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        // Mock userRepository.findByEmail to return the test user
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Mock otpRepository.findByUser to return empty (no existing OTP)
        when(otpRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // mock otpRepository.save to return the saved OTP
        ArgumentCaptor<PasswordResetOtp> otpCaptor = ArgumentCaptor.forClass(PasswordResetOtp.class);
        when(otpRepository.save(otpCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        otpService.generatePasswordResetOtp(PasswordResetRequestDto.builder()
                .email("test@example.com")
                .build());

        verify(otpRepository).findByUser(testUser);
        verify(otpRepository, never()).deleteByUser(testUser);
        verify(otpRepository).save(otpCaptor.capture());
        PasswordResetOtp savedOtp = otpCaptor.getValue();
        assertThat(savedOtp.getUser()).isEqualTo(testUser);
        assertThat(savedOtp.getOtp()).isNotNull();
        assertThat(savedOtp.getOtp().length()).isEqualTo(6);
        assertThat(savedOtp.getOtp()).matches("\\d{6}");
        assertThat(savedOtp.getExpiresAt()).isAfter(LocalDateTime.now().plusMinutes(9));
        assertThat(savedOtp.getExpiresAt()).isBefore(LocalDateTime.now().plusMinutes(11));
    }

    @Test
    void generatePasswordResetOtp_whenOtpExists_thenReplacesOtp() {
        // Create a test user
        HeapDogUser testUser = new HeapDogUser();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        // Create an existing OTP
        PasswordResetOtp existingOtp = new PasswordResetOtp();
        existingOtp.setId(1L);
        existingOtp.setUser(testUser);
        existingOtp.setOtp("123456");
        existingOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        // Mock userRepository.findByEmail to return the test user
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Mock otpRepository.findByUser to return the existing OTP
        when(otpRepository.findByUser(testUser)).thenReturn(Optional.of(existingOtp));

        // mock otpRepository.save to return the saved OTP
        ArgumentCaptor<PasswordResetOtp> otpCaptor = ArgumentCaptor.forClass(PasswordResetOtp.class);
        when(otpRepository.save(otpCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        otpService.generatePasswordResetOtp(PasswordResetRequestDto.builder()
                .email("test@example.com")
                .build());

        verify(otpRepository).findByUser(testUser);
        verify(otpRepository).deleteByUser(testUser);
        verify(otpRepository).save(otpCaptor.capture());
        PasswordResetOtp savedOtp = otpCaptor.getValue();
        assertThat(savedOtp.getUser()).isEqualTo(testUser);
        assertThat(savedOtp.getOtp()).isNotNull();
        assertThat(savedOtp.getOtp().length()).isEqualTo(6);
        assertThat(savedOtp.getOtp()).matches("\\d{6}");
        assertThat(savedOtp.getExpiresAt()).isAfter(LocalDateTime.now().plusMinutes(9));
        assertThat(savedOtp.getExpiresAt()).isBefore(LocalDateTime.now().plusMinutes(11));
    }
}