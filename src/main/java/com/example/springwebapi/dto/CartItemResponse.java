package com.example.springwebapi.dto;

import com.example.springwebapi.entity.CartItem;
import lombok.Data;

@Data
public class CartItemResponse {

    private Long id;
    private Long customerId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double subtotal;

    public static CartItemResponse fromEntity(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setCustomerId(cartItem.getCustomer().getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setProductPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setSubtotal(cartItem.getSubtotal());
        return response;
    }
} 