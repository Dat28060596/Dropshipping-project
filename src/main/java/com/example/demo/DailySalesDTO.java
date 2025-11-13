package com.example.demo;

import java.time.LocalDate;

/**
 * A DTO for carrying daily sales data.
 */
public class DailySalesDTO {

    private final LocalDate date;
    private final double totalSales;

    // Constructor
    public DailySalesDTO(LocalDate date, double totalSales) {
        this.date = date;
        this.totalSales = totalSales;
    }

    // --- Getters ---
    // (These are ESSENTIAL for Spring to convert this object to JSON)
    
    public LocalDate getDate() {
        return date;
    }

    public double getTotalSales() {
        return totalSales;
    }
    
    // You can also add setters if needed, but for a DTO, they aren't always required.
}