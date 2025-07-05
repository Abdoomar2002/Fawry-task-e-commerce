package com.example.springwebapi.entity;

import com.example.springwebapi.model.Expirable;
import com.example.springwebapi.model.Shippable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("SHIPPABLE_EXPIRABLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShippableExpirableProduct extends Product implements Shippable, Expirable {
    private double weight;
    private LocalDate expiryDate;

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }
} 