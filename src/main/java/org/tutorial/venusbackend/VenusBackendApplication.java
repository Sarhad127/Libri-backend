package org.tutorial.venusbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.MyUserRepository;

@SpringBootApplication
public class VenusBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenusBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdminUser(
            MyUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String adminEmail = "admin@libri.com";

            boolean adminExists = userRepository.findByEmail(adminEmail).isPresent();

            if (!adminExists) {
                MyUser admin = new MyUser();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("asd"));
                admin.setRole(MyUser.Role.ADMIN);
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setActive(true);

                userRepository.save(admin);

            } else {
            }
        };
    }
}