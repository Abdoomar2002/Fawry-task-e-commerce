package com.example.springwebapi.entity;

import com.example.springwebapi.model.Shippable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("SHIPPABLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShippableProduct extends Product implements Shippable {
    private double weight;

    @Override
    public double getWeight() {
        return weight;
    }
} 