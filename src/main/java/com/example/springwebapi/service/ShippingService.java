package com.example.springwebapi.service;

import java.util.List;

public interface ShippingService {
    String shipItems(List<ShippableItem> items);
} 