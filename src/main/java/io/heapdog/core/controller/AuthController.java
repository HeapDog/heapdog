package io.heapdog.core.controller;


import io.heapdog.core.dto.SigninRequestDto;
import io.heapdog.core.dto.SigninResponseDto;
import io.heapdog.core.dto.SignupRequestDto;
import io.heapdog.core.dto.SignupResponseDto;
import io.heapdog.core.service.HeapDogUserService;
import com.nimbusds.jose.JOSEException;
import io.heapdog.core.service.JwtAuthenticationService;
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

}
