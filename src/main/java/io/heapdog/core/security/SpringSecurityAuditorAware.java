package io.heapdog.core.security;

import io.heapdog.core.feature.user.HeapDogUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<HeapDogUser> {
    @Override
    public Optional<HeapDogUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(authentication.getPrincipal())
                .filter(HeapDogUser.class::isInstance)
                .map(HeapDogUser.class::cast);
    }
}
