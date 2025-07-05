package com.example.springwebapi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("SIMPLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleProduct extends Product {
    // No extra fields
} 