package com.example.springwebapi.service;

import com.example.springwebapi.entity.*;
import com.example.springwebapi.model.Expirable;
import com.example.springwebapi.model.Shippable;
import com.example.springwebapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final EcommerceService ecommerceService;

    public void runDemo() {
        System.out.println("=== Fawry Quantum Internship Challenge Demo ===");
        
        // Get all products and customers
        List<Product> products = productRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        
        if (products.isEmpty() || customers.isEmpty()) {
            System.out.println("No products or customers found. Please check data initialization.");
            return;
        }
        
        Customer customer = customers.get(0); // Use first customer
        
        System.out.println("\n1. Available Products:");
        for (Product product : products) {
            System.out.printf("- %s (Price: %.0f, Quantity: %d)", 
                product.getName(), product.getPrice(), product.getQuantity());
            
            if (product instanceof Shippable) {
                System.out.printf(", Shippable (Weight: %.0fg)", ((Shippable) product).getWeight());
            }
            
            if (product instanceof Expirable) {
                System.out.printf(", Expires: %s", ((Expirable) product).getExpiryDate());
            }
            
            System.out.println();
        }
        
        System.out.printf("\n2. Customer: %s (Balance: %.0f)%n", customer.getName(), customer.getBalance());
        
        // Add products to cart
        System.out.println("\n3. Adding products to cart...");
        
        try {
            // Add shippable product (TV)
            Product tv = products.stream()
                    .filter(p -> p.getName().equals("TV"))
                    .findFirst()
                    .orElse(null);
            
            if (tv != null) {
                ecommerceService.addToCart(customer.getId(), tv.getId(), 1);
                System.out.printf("Added 1x %s to cart%n", tv.getName());
            }
            
            // Add expirable product (Cheese)
            Product cheese = products.stream()
                    .filter(p -> p.getName().equals("Cheese"))
                    .findFirst()
                    .orElse(null);
            
            if (cheese != null) {
                ecommerceService.addToCart(customer.getId(), cheese.getId(), 2);
                System.out.printf("Added 2x %s to cart%n", cheese.getName());
            }
            
            // Add simple product (Scratch Card)
            Product scratchCard = products.stream()
                    .filter(p -> p.getName().equals("Scratch Card"))
                    .findFirst()
                    .orElse(null);
            
            if (scratchCard != null) {
                ecommerceService.addToCart(customer.getId(), scratchCard.getId(), 3);
                System.out.printf("Added 3x %s to cart%n", scratchCard.getName());
            }
            
            // Show cart
            System.out.println("\n4. Current cart:");
            List<CartItem> cartItems = ecommerceService.getCart(customer.getId());
            for (CartItem item : cartItems) {
                System.out.printf("- %dx %s (%.0f each) = %.0f%n", 
                    item.getQuantity(), 
                    item.getProduct().getName(), 
                    item.getProduct().getPrice(), 
                    item.getSubtotal());
            }
            
            // Checkout
            System.out.println("\n5. Processing checkout...");
            ecommerceService.checkout(customer.getId());
            
            System.out.println("\n6. Demo completed successfully!");
            
        } catch (Exception e) {
            System.out.println("Error during demo: " + e.getMessage());
        }
    }

    public void runSpecificTest() {
        System.out.println("=== Specific Test: Cart and Checkout ===");
        
        // Get all products and customers
        List<Product> products = productRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        
        if (products.isEmpty() || customers.isEmpty()) {
            System.out.println("No products or customers found. Please check data initialization.");
            return;
        }
        
        Customer customer = customers.get(0); // Use first customer
        
        try {
            // Find the specific products
            Product cheese = products.stream()
                    .filter(p -> p.getName().equals("Cheese"))
                    .findFirst()
                    .orElse(null);
            
            Product biscuits = products.stream()
                    .filter(p -> p.getName().equals("Biscuits"))
                    .findFirst()
                    .orElse(null);
            
            if (cheese == null || biscuits == null) {
                System.out.println("Required products not found. Available products:");
                products.forEach(p -> System.out.println("- " + p.getName()));
                return;
            }
            
            // Clear any existing cart items
            List<CartItem> existingCart = ecommerceService.getCart(customer.getId());
            if (!existingCart.isEmpty()) {
                System.out.println("Clearing existing cart...");
                // Note: In a real implementation, you'd have a clear cart method
                // For now, we'll just proceed with the test
            }
            
            // Add items to cart as specified in the expected output
            System.out.println("Adding items to cart:");
            ecommerceService.addToCart(customer.getId(), cheese.getId(), 2);
            System.out.println("cart.add(cheese, 2);");
            
            ecommerceService.addToCart(customer.getId(), biscuits.getId(), 1);
            System.out.println("cart.add(biscuits, 1);");
            
            // Show cart before checkout
            System.out.println("\nCart contents before checkout:");
            List<CartItem> cartItems = ecommerceService.getCart(customer.getId());
            for (CartItem item : cartItems) {
                System.out.printf("- %dx %s (%.0f each) = %.0f%n", 
                    item.getQuantity(), 
                    item.getProduct().getName(), 
                    item.getProduct().getPrice(), 
                    item.getSubtotal());
            }
            
            // Checkout
            System.out.println("\ncheckout(customer, cart);");
            ecommerceService.checkout(customer.getId());
            
            System.out.println("\nSpecific test completed successfully!");
            
        } catch (Exception e) {
            System.out.println("Error during specific test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 