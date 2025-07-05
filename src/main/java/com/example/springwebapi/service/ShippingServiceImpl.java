package com.example.springwebapi.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Override
    public void shipItems(List<ShippableItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        System.out.println("** Shipment notice **");
        
        double totalWeight = 0.0;
        for (ShippableItem item : items) {
            System.out.printf("%dx %s %.0fg%n", 1, item.getName(), item.getWeight());
            totalWeight += item.getWeight();
        }
        
        System.out.printf("Total package weight %.1fkg%n", totalWeight / 1000.0);
    }
} 