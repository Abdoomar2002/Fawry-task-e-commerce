package com.example.springwebapi.controller;

import com.example.springwebapi.entity.Product;
import com.example.springwebapi.factory.ProductFactory;
import com.example.springwebapi.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> body) {
        try {
            String name = body.get("name").toString();
            double price = Double.parseDouble(body.get("price").toString());
            int quantity = Integer.parseInt(body.get("quantity").toString());
            Double weight = body.get("weight") != null ? Double.valueOf(body.get("weight").toString()) : null;
            LocalDate expiryDate = body.get("expiryDate") != null ? LocalDate.parse(body.get("expiryDate").toString()) : null;
            Product product = ProductFactory.createProduct(name, price, quantity, weight, expiryDate);
            Product createdProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return productRepository.findById(id)
                .map(existing -> {
                    String name = body.get("name").toString();
                    double price = Double.parseDouble(body.get("price").toString());
                    int quantity = Integer.parseInt(body.get("quantity").toString());
                    Double weight = body.get("weight") != null ? Double.valueOf(body.get("weight").toString()) : null;
                    LocalDate expiryDate = body.get("expiryDate") != null ? LocalDate.parse(body.get("expiryDate").toString()) : null;
                    Product updated = ProductFactory.createProduct(name, price, quantity, weight, expiryDate);
                    updated.setId(existing.getId());
                    return ResponseEntity.ok(productRepository.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 