package com.example.ecommerce.data.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private final String category;
    private final double price;
    private final String description;
    private final String details;
    private final float rating;
    private final @DrawableRes int imageRes;

    public Product(String id,
                   String name,
                   String category,
                   double price,
                   String description,
                   String details,
                   float rating,
                   int imageRes) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.details = details;
        this.rating = rating;
        this.imageRes = imageRes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public float getRating() {
        return rating;
    }

    public int getImageRes() {
        return imageRes;
    }
}

