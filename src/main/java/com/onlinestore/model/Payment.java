/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onlinestore.model;

/**
 *
 * @author Admin
 */
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private String orderId;
    private Integer userId;
    private String vnpTxnRef;
    private String vnpTransactionNo;
    private long vnpAmount;
    private String vnpBankCode;
    private String vnpCardType;
    private String vnpResponseCode;
    private String paymentStatus;
    private Timestamp vnpPayDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String vnpOrderInfo;
    private String ipAddress;
    
    // Constructors
    public Payment() {}
    
    public Payment(String orderId, String vnpTxnRef, long vnpAmount, String paymentStatus) {
        this.orderId = orderId;
        this.vnpTxnRef = vnpTxnRef;
        this.vnpAmount = vnpAmount;
        this.paymentStatus = paymentStatus;
    }
    
    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getVnpTxnRef() { return vnpTxnRef; }
    public void setVnpTxnRef(String vnpTxnRef) { this.vnpTxnRef = vnpTxnRef; }
    
    public String getVnpTransactionNo() { return vnpTransactionNo; }
    public void setVnpTransactionNo(String vnpTransactionNo) { 
        this.vnpTransactionNo = vnpTransactionNo; 
    }
    
    public long getVnpAmount() { return vnpAmount; }
    public void setVnpAmount(long vnpAmount) { this.vnpAmount = vnpAmount; }
    
    public String getVnpBankCode() { return vnpBankCode; }
    public void setVnpBankCode(String vnpBankCode) { this.vnpBankCode = vnpBankCode; }
    
    public String getVnpCardType() { return vnpCardType; }
    public void setVnpCardType(String vnpCardType) { this.vnpCardType = vnpCardType; }
    
    public String getVnpResponseCode() { return vnpResponseCode; }
    public void setVnpResponseCode(String vnpResponseCode) { 
        this.vnpResponseCode = vnpResponseCode; 
    }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { 
        this.paymentStatus = paymentStatus; 
    }
    
    public Timestamp getVnpPayDate() { return vnpPayDate; }
    public void setVnpPayDate(Timestamp vnpPayDate) { this.vnpPayDate = vnpPayDate; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public String getVnpOrderInfo() { return vnpOrderInfo; }
    public void setVnpOrderInfo(String vnpOrderInfo) { this.vnpOrderInfo = vnpOrderInfo; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}