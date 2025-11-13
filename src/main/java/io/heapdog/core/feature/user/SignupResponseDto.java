package io.heapdog.core.feature.user;


import lombok.*;

@Getter
@Setter
@Builder
public class SignupResponseDto {
    private Long id;
    private String username;
    private String email;
}
