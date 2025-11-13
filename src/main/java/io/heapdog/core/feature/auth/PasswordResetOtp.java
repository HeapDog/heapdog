package io.heapdog.core.feature.auth;


import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otp")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetOtp extends BaseEntity {

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
