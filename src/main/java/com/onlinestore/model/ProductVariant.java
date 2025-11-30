package com.onlinestore.model;

import java.util.Date;

public class ProductVariant {
    private int id;
    private int productId;
    private String sku;
    private String size;
    private String color;
    private String material;
    private double price;  // ADD THIS FIELD
    private int stockQuantity;
    private int lowStockThreshold;
    private String imageUrl;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    // ADD THESE TWO METHODS:
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int threshold) { 
        this.lowStockThreshold = threshold; 
    }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold;
    }
    
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        if (size != null && !size.isEmpty()) sb.append("Size: ").append(size);
        if (color != null && !color.isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("Color: ").append(color);
        }
        if (material != null && !material.isEmpty()) {
            if (sb.length() > 0) sb.append(" | ");
            sb.append("Material: ").append(material);
        }
        return sb.toString();
    }
}
