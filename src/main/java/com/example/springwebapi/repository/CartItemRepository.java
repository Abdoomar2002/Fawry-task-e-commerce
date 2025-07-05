package com.example.springwebapi.repository;

import com.example.springwebapi.entity.CartItem;
import com.example.springwebapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByCustomer(Customer customer);
    
    Optional<CartItem> findByCustomerAndProductId(Customer customer, Long productId);
    
    void deleteByCustomer(Customer customer);
} 