package io.heapdog.core.feature.auth;


import io.heapdog.core.feature.user.HeapDogUser;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Data
@Builder
public class SigninResponseDto {

    private String token;
}
