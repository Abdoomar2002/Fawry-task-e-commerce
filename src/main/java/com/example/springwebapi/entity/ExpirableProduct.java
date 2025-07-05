package com.example.springwebapi.entity;

import com.example.springwebapi.model.Expirable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("EXPIRABLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpirableProduct extends Product implements Expirable {
    private LocalDate expiryDate;

    @Override
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }
} 