package com.example.springwebapi.factory;

import com.example.springwebapi.entity.*;

import java.time.LocalDate;

public class ProductFactory {
    public static Product createProduct(String name, double price, int quantity, Double weight, LocalDate expiryDate) {
        boolean hasWeight = weight != null && weight > 0;
        boolean hasExpiry = expiryDate != null;

        if (hasWeight && hasExpiry) {
            ShippableExpirableProduct p = new ShippableExpirableProduct();
            p.setName(name);
            p.setPrice(price);
            p.setQuantity(quantity);
            p.setWeight(weight);
            p.setExpiryDate(expiryDate);
            return p;
        } else if (hasWeight) {
            ShippableProduct p = new ShippableProduct();
            p.setName(name);
            p.setPrice(price);
            p.setQuantity(quantity);
            p.setWeight(weight);
            return p;
        } else if (hasExpiry) {
            ExpirableProduct p = new ExpirableProduct();
            p.setName(name);
            p.setPrice(price);
            p.setQuantity(quantity);
            p.setExpiryDate(expiryDate);
            return p;
        } else {
            SimpleProduct p = new SimpleProduct();
            p.setName(name);
            p.setPrice(price);
            p.setQuantity(quantity);
            return p;
        }
    }
} 