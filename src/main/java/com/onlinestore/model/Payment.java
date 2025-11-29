package com.onlinestore.model;

import java.sql.Timestamp;

public class Payment {
    private int id;
    private String orderId;
    private String userId;
    private String vnpTxnRef;
    private long vnpAmount;
    private String vnpOrderInfo;
    private String vnpTransactionNo;
    private String vnpResponseCode;
    private String vnpBankCode;
    private String vnpCardType;
    private Timestamp vnpPayDate;
    private String paymentStatus;
    private String ipAddress;
    private Timestamp createdDate;
    
    // Constructors
    public Payment() {}
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getVnpTxnRef() {
        return vnpTxnRef;
    }
    
    public void setVnpTxnRef(String vnpTxnRef) {
        this.vnpTxnRef = vnpTxnRef;
    }
    
    public long getVnpAmount() {
        return vnpAmount;
    }
    
    public void setVnpAmount(long vnpAmount) {
        this.vnpAmount = vnpAmount;
    }
    
    public String getVnpOrderInfo() {
        return vnpOrderInfo;
    }
    
    public void setVnpOrderInfo(String vnpOrderInfo) {
        this.vnpOrderInfo = vnpOrderInfo;
    }
    
    public String getVnpTransactionNo() {
        return vnpTransactionNo;
    }
    
    public void setVnpTransactionNo(String vnpTransactionNo) {
        this.vnpTransactionNo = vnpTransactionNo;
    }
    
    public String getVnpResponseCode() {
        return vnpResponseCode;
    }
    
    public void setVnpResponseCode(String vnpResponseCode) {
        this.vnpResponseCode = vnpResponseCode;
    }
    
    public String getVnpBankCode() {
        return vnpBankCode;
    }
    
    public void setVnpBankCode(String vnpBankCode) {
        this.vnpBankCode = vnpBankCode;
    }
    
    public String getVnpCardType() {
        return vnpCardType;
    }
    
    public void setVnpCardType(String vnpCardType) {
        this.vnpCardType = vnpCardType;
    }
    
    public Timestamp getVnpPayDate() {
        return vnpPayDate;
    }
    
    public void setVnpPayDate(Timestamp vnpPayDate) {
        this.vnpPayDate = vnpPayDate;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
