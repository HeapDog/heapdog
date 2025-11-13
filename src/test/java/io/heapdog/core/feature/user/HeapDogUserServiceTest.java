package io.heapdog.core.feature.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HeapDogUserServiceTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private HeapDogUserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private HeapDogUserService userService;

    @Test
    void createUser_withValidPassword_shouldEncodePasswordBeforeSaving() {
        // Given
        SignupRequestDto requestDto = SignupRequestDto
                .builder()
                .username("test")
                .email("test@example.com")
                .password("rawpassword")
                .build();

        // When
        when(encoder.encode(any(CharSequence.class))).thenReturn("encodedpassword");
        when(repository.save(any(HeapDogUser.class))).thenReturn(HeapDogUser.builder().build());
        when(mapper.toEntity(any(SignupRequestDto.class))).thenReturn(HeapDogUser.builder().build());
        when(mapper.toDto(any(HeapDogUser.class))).thenReturn(SignupResponseDto.builder().build());

        userService.createUser(requestDto);

        InOrder inOrder = inOrder(encoder, repository, mapper, encoder);
        inOrder.verify(encoder).encode(any(CharSequence.class));
        inOrder.verify(mapper).toEntity(any(SignupRequestDto.class));
        inOrder.verify(repository).save(any(HeapDogUser.class));
        inOrder.verify(mapper).toDto(any(HeapDogUser.class));
    }

    @Test
    void createUser_withValidUser_shouldReturnSignupResponseDto() {

        // Given
        HeapDogUser user = HeapDogUser.builder()
                .username("test")
                .email("test@example.com")
                .password("")
                .role(Set.of(HeapDogUser.Role.ROLE_USER))
                .build();

        when(encoder.encode(any(CharSequence.class))).thenReturn("password");
        when(repository.save(any(HeapDogUser.class))).thenReturn(user);
        when(mapper.toEntity(any(SignupRequestDto.class))).thenReturn(user);
        when(mapper.toDto(any(HeapDogUser.class))).thenReturn(SignupResponseDto
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build());
        // When
        var response = userService.createUser(SignupRequestDto
                .builder()
                        .email(user.getEmail())
                .username(user.getUsername())
                .password("password")
                .build());

        Assertions.assertNotNull(response);
    }
}