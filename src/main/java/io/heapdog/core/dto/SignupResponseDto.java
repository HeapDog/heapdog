package io.heapdog.core.dto;


import lombok.*;

@Getter
@Setter
public class SignupResponseDto {
    private Long id;
    private String username;
    private String email;
}
