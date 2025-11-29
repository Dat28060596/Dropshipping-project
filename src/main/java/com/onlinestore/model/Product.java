package com.onlinestore.model;

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
}