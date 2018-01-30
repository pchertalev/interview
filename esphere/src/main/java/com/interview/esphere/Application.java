package com.interview.esphere;

import com.interview.esphere.domain.model.User;
import com.interview.esphere.domain.repository.UserRepository;
import com.interview.esphere.domain.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            Role adminRole = new Role("ADMIN");

            User user = new User();
            user.setName("admin");
            user.setEmail("admin@mail.com");
            user.setPassword("123456");
            user.setLastName("admin");

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActive(1);
            user.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            userRepository.save(user);

        };
    }
}
