package com.onlinestore.model;

import java.util.List;
import java.util.Date;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private int supplierId; 
    private int categoryId; 
    private String categoryName;
    
    // NEW FIELDS - Added for enhanced product catalog management
    private String detailedDescription;
    private boolean hasVariants;
    private String specifications; // JSON string
    private String[] tags;
    private List<ProductVariant> variants;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    

    public Product() {}

    public Product(int id, String name, double price, int stock, int supplierId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.supplierId = supplierId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    // NEW GETTERS AND SETTERS
    
    public String getDetailedDescription() { return detailedDescription; }
    public void setDetailedDescription(String detailedDescription) { 
        this.detailedDescription = detailedDescription; 
    }
    
    public boolean isHasVariants() { return hasVariants; }
    public void setHasVariants(boolean hasVariants) { this.hasVariants = hasVariants; }
    
    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { 
        this.specifications = specifications; 
    }
    
    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }
    
    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
