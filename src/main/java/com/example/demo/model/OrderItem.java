package com.example.demo.model;

import jakarta.persistence.*;

/**
 * OrderItem Entity - Represents individual items within an order
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPriceAtTimeOfSale; // Price customer paid per unit

    @Column(nullable = false)
    private Double supplierCostAtTimeOfSale; // Cost from supplier per unit

    // ===== Constructors =====

    public OrderItem() {}

    public OrderItem(Order order, String productName, Integer quantity, 
                     Double unitPriceAtTimeOfSale, Double supplierCostAtTimeOfSale) {
        this.order = order;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPriceAtTimeOfSale = unitPriceAtTimeOfSale;
        this.supplierCostAtTimeOfSale = supplierCostAtTimeOfSale;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPriceAtTimeOfSale() {
        return unitPriceAtTimeOfSale;
    }

    public void setUnitPriceAtTimeOfSale(Double unitPriceAtTimeOfSale) {
        this.unitPriceAtTimeOfSale = unitPriceAtTimeOfSale;
    }

    public Double getSupplierCostAtTimeOfSale() {
        return supplierCostAtTimeOfSale;
    }

    public void setSupplierCostAtTimeOfSale(Double supplierCostAtTimeOfSale) {
        this.supplierCostAtTimeOfSale = supplierCostAtTimeOfSale;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPriceAtTimeOfSale=" + unitPriceAtTimeOfSale +
                ", supplierCostAtTimeOfSale=" + supplierCostAtTimeOfSale +
                '}';
    }
}
