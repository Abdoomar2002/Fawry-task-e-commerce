package com.example.springwebapi.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Override
    public String shipItems(List<ShippableItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("** Shipment notice **\n");

        // Group items by name and track count and weight
        Map<String, Integer> itemCounts = new HashMap<>();
        Map<String, Double> itemWeights = new HashMap<>();
        double totalWeight = 0.0;

        for (ShippableItem item : items) {
            String name = item.getName();
            itemCounts.put(name, itemCounts.getOrDefault(name, 0) + 1);
            itemWeights.put(name, item.getWeight());
            totalWeight += item.getWeight();
        }

        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            double weight = itemWeights.get(name); // per item weight
            sb.append(String.format("%dx %s %.0fg%n", count, name, weight));
        }

        sb.append(String.format("Total package weight %.1fkg%n", totalWeight / 1000.0));

        System.out.printf(sb.toString());
        return sb.toString();
    }
} 