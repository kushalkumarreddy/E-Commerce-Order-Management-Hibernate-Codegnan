
package com.ecommerce.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private double price;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders = new HashSet<>();

    public Product() {
    }

    public Product(String productName, double price) {
        this.productName = productName;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public Set<Order> getOrders() {
        return orders;
    }
}
