package io.heapdog.core;

import io.heapdog.core.model.HeapDogUser;
import io.heapdog.core.repository.HeapDogUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class HeapDogApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeapDogApplication.class, args);
	}


    @Bean
    CommandLineRunner commandLineRunner(HeapDogUserRepository repository,
                                        PasswordEncoder encoder) {

        log.info("Running command line runner");
        return _ -> {
            var user = HeapDogUser
                    .builder()
                    .username("parthokr")
                    .email("partho.kr@gmail.com")
                    .password(encoder.encode("1234"))
                    .role(Set.of(HeapDogUser.Role.ROLE_USER, HeapDogUser.Role.ROLE_ADMIN))
                    .build();

            repository.save(user);
        };
    }

}
