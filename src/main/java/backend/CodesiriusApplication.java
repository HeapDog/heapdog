package backend;

import backend.model.CodesiriusUser;
import backend.repository.CodesiriusUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class CodesiriusApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodesiriusApplication.class, args);
	}


    @Bean
    CommandLineRunner commandLineRunner(CodesiriusUserRepository repository,
                                        PasswordEncoder encoder) {
        return args -> {
            var user = CodesiriusUser
                    .builder()
                    .username("parthokr")
                    .email("partho.kr@gmail.com")
                    .password(encoder.encode("1234"))
                    .role(Set.of(CodesiriusUser.Role.ROLE_USER))
                    .build();

            repository.save(user);
        };
    }

}
