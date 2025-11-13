package com.example.demo;

public class ProfitabilityReport {

    private final double totalRevenue;
    private final double totalCogs;        // Cost of Goods Sold
    private final double grossProfit;
    private final double profitMargin;

    // Constructor
    public ProfitabilityReport(double totalRevenue, double totalCogs, double grossProfit, double profitMargin) {
        this.totalRevenue = totalRevenue;
        this.totalCogs = totalCogs;
        this.grossProfit = grossProfit;
        this.profitMargin = profitMargin;
    }

    // --- Getters ---
    // (Again, these are ESSENTIAL for the JSON conversion)

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalCogs() {
        return totalCogs;
    }

    public double getGrossProfit() {
        return grossProfit;
    }

    public double getProfitMargin() {
        return profitMargin;
    }
}