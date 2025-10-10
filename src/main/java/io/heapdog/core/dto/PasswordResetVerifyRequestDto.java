package io.heapdog.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class PasswordResetVerifyRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "OTP is required")
    private String otp;
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password should be at least 8 character long")
    private String newPassword;
}
