package io.heapdog.core.feature.auth;

import io.heapdog.core.feature.user.HeapDogUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findByUser(HeapDogUser user);

    @Transactional
    void deleteByUser(HeapDogUser user);
}