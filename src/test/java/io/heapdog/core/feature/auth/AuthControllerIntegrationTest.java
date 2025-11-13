package io.heapdog.core.feature.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.feature.user.HeapDogUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIntegrationTest {
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
                .role(HeapDogUser.Role.ROLE_USER)
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
