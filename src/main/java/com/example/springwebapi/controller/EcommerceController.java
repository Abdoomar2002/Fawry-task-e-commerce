package com.example.springwebapi.controller;

import com.example.springwebapi.dto.CartItemRequest;
import com.example.springwebapi.dto.CartItemResponse;
import com.example.springwebapi.entity.CartItem;
import com.example.springwebapi.service.EcommerceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ecommerce")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EcommerceController {

    private final EcommerceService ecommerceService;

    @PostMapping("/cart/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody CartItemRequest request) {
        try {
            ecommerceService.addToCart(request.getCustomerId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cart/{customerId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Long customerId) {
        try {
            List<CartItem> cartItems = ecommerceService.getCart(customerId);
            List<CartItemResponse> responses = cartItems.stream()
                    .map(CartItemResponse::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<String> checkout(@PathVariable Long customerId) {
        try {
            ecommerceService.checkout(customerId);
            return ResponseEntity.ok("Checkout completed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/cart/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long customerId, @RequestParam Long productId) {
        try {
            ecommerceService.removeFromCart(customerId, productId);
            return ResponseEntity.ok("Item removed from cart successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cart/update")
    public ResponseEntity<String> updateCartItem(@Valid @RequestBody CartItemRequest request) {
        try {
            ecommerceService.updateCartItem(request.getCustomerId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok("Cart item updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cart/clear/{customerId}")
    public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
        try {
            ecommerceService.clearCart(customerId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 