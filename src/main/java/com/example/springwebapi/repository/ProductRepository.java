package com.example.springwebapi.repository;

import com.example.springwebapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByQuantityGreaterThan(Integer quantity);
} 