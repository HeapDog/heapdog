package io.heapdog.core.config;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class JpaConfig {

    @Bean
    public AuditorAware<HeapDogUser> springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }

}
