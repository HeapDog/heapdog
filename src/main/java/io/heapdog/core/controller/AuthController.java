package io.heapdog.core.controller;


import io.heapdog.core.dto.*;
import io.heapdog.core.service.HeapDogUserService;
import com.nimbusds.jose.JOSEException;
import io.heapdog.core.service.JwtAuthenticationService;
import io.heapdog.core.service.OtpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final HeapDogUserService service;
    private final JwtAuthenticationService jwtService;
    private final OtpService otpService;

    @PostMapping("/signin")
    ResponseEntity<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto dto) throws JOSEException {
        SigninResponseDto res = jwtService.authenticate(dto);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/signup")
    ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto dto) {
        SignupResponseDto res = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/reset")
    public ResponseEntity<PasswordResetResponseDto> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDto dto) {

        PasswordResetResponseDto res = otpService.generatePasswordResetOtp(dto);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PatchMapping("/reset/verify")
    public ResponseEntity<PasswordResetResponseDto> verifyOtpAndResetPassword(@Valid @RequestBody PasswordResetVerifyRequestDto dto){

        PasswordResetResponseDto res = otpService.verifyOtpAndResetPassword(dto);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
