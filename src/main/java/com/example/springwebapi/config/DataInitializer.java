package com.example.springwebapi.config;

import com.example.springwebapi.entity.User;
import com.example.springwebapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        userRepository.deleteAll();

        // Create sample users
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("Bob Johnson");
        user3.setEmail("bob.johnson@example.com");
        userRepository.save(user3);

        System.out.println("Sample data initialized successfully!");
    }
} 