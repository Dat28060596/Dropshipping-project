package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
// Allow requests from your React app (which runs on a different port, e.g., 3000)
@CrossOrigin(origins = "http://localhost:3000") 
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Use constructor injection
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * API Endpoint for the "Sales by Day" report.
     * React will call: GET /api/analytics/sales-by-day?startDate=2023-01-01&endDate=2023-01-31
     */
    @GetMapping("/sales-by-day")
    public List<DailySalesDTO> getSalesByDay(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        
        // The DTO (DailySalesDTO) will be automatically converted to JSON
        return analyticsService.getSalesByDay(startDate, endDate);
    }

    /**
     * API Endpoint for the "Profitability" report.
     * React will call: GET /api/analytics/profitability?startDate=...&endDate=...
     */
    @GetMapping("/profitability")
    public ProfitabilityReport getProfitability(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        
        return analyticsService.getProfitabilityReport(startDate, endDate);
    }
    
    /**
     * API Endpoint for "Top Selling Products".
     * React will call: GET /api/analytics/top-products?startDate=...&endDate=...&limit=5
     */
    // @GetMapping("/top-products")
    // public List<ProductPerformanceDTO> getTopProducts(
    //         @RequestParam("startDate") LocalDate startDate,
    //         @RequestParam("endDate") LocalDate endDate,
    //         @RequestParam(value = "limit", defaultValue = "5") int limit) {
    //             
    //     return analyticsService.getTopSellingProducts(startDate, endDate, limit);
    // }

    // ... add other endpoints for AOV, top customers, etc.
}