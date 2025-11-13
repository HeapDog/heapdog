package io.heapdog.core;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.feature.user.HeapDogUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class HeapDogApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeapDogApplication.class, args);
	}


    @Bean
    CommandLineRunner commandLineRunner(HeapDogUserRepository userRepository, PasswordEncoder encoder) {

        log.info("Running command line runner to seed database...");
        return _ -> {

            HeapDogUser demoUser = HeapDogUser
                    .builder()
                    .username("demo")
                    .email("demo@example.com")
                    .password(encoder.encode("demo1234"))
                    .role(HeapDogUser.Role.ROLE_USER)
                    .build();

            HeapDogUser standardUser = HeapDogUser
                    .builder()
                    .username("user")
                    .email("user@example.com")
                    .password(encoder.encode("user1234"))
                    .role(HeapDogUser.Role.ROLE_USER)
                    .build();

            HeapDogUser adminUser = HeapDogUser
                    .builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(encoder.encode("admin1234"))
                    .role(HeapDogUser.Role.ROLE_ADMIN)
                    .build();

//            userRepository.saveAll(List.of(demoUser, standardUser, adminUser));
            log.info("Database seeded with 3 users.");
        };
    }

}
