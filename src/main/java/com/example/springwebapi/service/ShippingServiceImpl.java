package com.example.springwebapi.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Override
    public void shipItems(List<ShippableItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        System.out.println("** Shipment notice **");
        
        // Group items by name and sum their weights
        Map<String, Integer> itemCounts = new HashMap<>();
        Map<String, Double> itemWeights = new HashMap<>();
        double totalWeight = 0.0;
        
        for (ShippableItem item : items) {
            String name = item.getName();
            itemCounts.put(name, itemCounts.getOrDefault(name, 0) + 1);
            itemWeights.put(name, item.getWeight());
            totalWeight += item.getWeight();
        }
        
        // Print grouped items
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            double weight = itemWeights.get(name);
            System.out.printf("%dx %s %.0fg%n", count, name, weight);
        }
        
        System.out.printf("Total package weight %.1fkg%n", totalWeight / 1000.0);
    }
} 