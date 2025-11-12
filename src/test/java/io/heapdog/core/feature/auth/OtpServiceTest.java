package io.heapdog.core.feature.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.feature.user.HeapDogUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @Transactional
    public static class AuthControllerIntegrationTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private HeapDogUserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private PasswordResetOtpRepository otpRepository;

        @BeforeEach
        void setUp() {
            HeapDogUser user = HeapDogUser.builder()
                    .username("testuser")
                    .email("test@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(new HashSet<>(Set.of(HeapDogUser.Role.ROLE_USER)))
                    .build();
            userRepository.save(user);
        }

        @Test
        void whenRequestPasswordReset_withValidEmail_shouldReturnOk() throws Exception {

            PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
            requestDto.setEmail("test@example.com");

            mockMvc.perform(post("/auth/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("OTP sent successfully (mock)."));

            HeapDogUser user = userRepository.findByEmail("test@example.com").orElseThrow();
            PasswordResetOtp otp = otpRepository.findByUser(user).orElseThrow();

            assertThat(otp).isNotNull();
            assertThat(otp.getOtp()).isNotNull().isNotEmpty();
            assertThat(otp.getExpiresAt()).isNotNull();
        }

        @Test
        void whenRequestPasswordReset_withInvalidEmail_shouldReturnNotFound() throws Exception {

            PasswordResetRequestDto requestDto = new PasswordResetRequestDto();
            requestDto.setEmail("nobody@example.com");

            mockMvc.perform(post("/auth/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
        }
    }
}