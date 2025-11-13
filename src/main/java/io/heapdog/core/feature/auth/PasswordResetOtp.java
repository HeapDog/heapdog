package io.heapdog.core.feature.auth;


import io.heapdog.core.feature.user.HeapDogUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne(targetEntity = HeapDogUser.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private HeapDogUser user;
}
