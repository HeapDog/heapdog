package io.heapdog.core.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeapDogUserRepository extends JpaRepository<HeapDogUser, Long> {
    Optional<HeapDogUser> findByUsername(String username);
    Optional<HeapDogUser> findByEmail(String email);
}
