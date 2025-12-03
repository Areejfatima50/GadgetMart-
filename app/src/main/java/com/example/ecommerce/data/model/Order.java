package com.example.ecommerce.data.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private final String id;
    private final long placedAt;
    private final List<CartItem> items;
    private final double total;

    public Order(String id, long placedAt, List<CartItem> items, double total) {
        this.id = id;
        this.placedAt = placedAt;
        this.items = items;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public long getPlacedAt() {
        return placedAt;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}

