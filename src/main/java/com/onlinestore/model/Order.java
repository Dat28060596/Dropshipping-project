package com.onlinestore.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private double totalPrice;
    private String status;
    private Timestamp createdAt;
    private String shippingAddress;
    private String customerPhone;

    public Order() {}
    
    public Order(int userId, double totalPrice, String status) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}