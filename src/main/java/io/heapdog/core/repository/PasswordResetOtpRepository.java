package io.heapdog.core.repository;

import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.model.PasswordResetOtp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findByUser(HeapDogUser user);

    @Transactional
    void deleteByUser(HeapDogUser user);
}
