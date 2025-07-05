package com.example.springwebapi.service;

import java.util.List;

public interface ShippingService {
    void shipItems(List<ShippableItem> items);
} 