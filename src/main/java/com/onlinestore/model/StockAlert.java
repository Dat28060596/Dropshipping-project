package com.onlinestore.model;

import java.util.Date;

public class StockAlert {
    private int id;
    private int productId;
    private Integer variantId;
    private String productName;
    private String variantInfo;
    private String alertType;
    private int currentStock;
    private int thresholdValue;
    private String status;
    private Integer acknowledgedBy;
    private Date acknowledgedAt;
    private Date resolvedAt;
    private Date createdAt;
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public Integer getVariantId() { return variantId; }
    public void setVariantId(Integer variantId) { this.variantId = variantId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getVariantInfo() { return variantInfo; }
    public void setVariantInfo(String variantInfo) { this.variantInfo = variantInfo; }
    
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    
    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }
    
    public int getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(int thresholdValue) { 
        this.thresholdValue = thresholdValue; 
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(Integer acknowledgedBy) { 
        this.acknowledgedBy = acknowledgedBy; 
    }
    
    public Date getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(Date acknowledgedAt) { 
        this.acknowledgedAt = acknowledgedAt; 
    }
    
    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
