package com.example.springwebapi.config;

import com.example.springwebapi.entity.*;
import com.example.springwebapi.factory.ProductFactory;
import com.example.springwebapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        productRepository.deleteAll();
        customerRepository.deleteAll();

        // Create sample customers
        Customer customer1 = new Customer();
        customer1.setName("Alice Customer");
        customer1.setEmail("alice@example.com");
        customer1.setBalance(10000.0);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Bob Customer");
        customer2.setEmail("bob@example.com");
        customer2.setBalance(5000.0);
        customerRepository.save(customer2);

        // Create sample products using ProductFactory
        Product cheese = ProductFactory.createProduct("Cheese", 100.0, 10, 400.0, LocalDate.now().plusDays(30));
        productRepository.save(cheese);

        Product biscuits = ProductFactory.createProduct("Biscuits", 150.0, 15, 700.0, LocalDate.now().plusDays(60));
        productRepository.save(biscuits);

        Product tv = ProductFactory.createProduct("TV", 2000.0, 5, 15000.0, null);
        productRepository.save(tv);

        Product mobile = ProductFactory.createProduct("Mobile", 1500.0, 8, 200.0, null);
        productRepository.save(mobile);

        Product scratchCard = ProductFactory.createProduct("Scratch Card", 50.0, 100, null, null);
        productRepository.save(scratchCard);

        System.out.println("Sample data initialized successfully!");
        System.out.println("Created:");
        System.out.println("- 3 Users");
        System.out.println("- 2 Customers (Alice with 1000 balance, Bob with 500 balance)");
        System.out.println("- 5 Products (Cheese, Biscuits, TV, Mobile, Scratch Card)");
    }
} 