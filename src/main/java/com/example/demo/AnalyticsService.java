package com.example.demo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;

@Service // Tells Spring this is a Service class
public class AnalyticsService {

    // The service needs access to the database repositories
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // Constructor for dependency injection
    public AnalyticsService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Calculates total sales, grouped by day, for a date range.
     */
    public List<DailySalesDTO> getSalesByDay(LocalDate startDate, LocalDate endDate) {
        // Fetch all orders within the date range
        List<Order> orders = orderRepository.findAllByOrderDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        
        // Group orders by date and sum their totals
        Map<LocalDate, Double> salesByDay = orders.stream()
            .collect(Collectors.groupingBy(
                order -> order.getOrderDate().toLocalDate(), // Group by the date
                Collectors.summingDouble(order -> order.getTotalPrice()) // Sum totals for each date
            ));
            
        // Convert the Map into a sorted List of DTOs for the report
        return salesByDay.entrySet().stream()
            .map(entry -> new DailySalesDTO(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(DailySalesDTO::getDate))
            .collect(Collectors.toList());
    }

    /**
     * Calculates the profitability report for a date range.
     */
    public ProfitabilityReport getProfitabilityReport(LocalDate startDate, LocalDate endDate) {
        // Fetch all order *items* in the period
        List<OrderItem> items = orderItemRepository.findAllByOrder_OrderDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        
        double totalRevenue = 0;
        double totalCogs = 0; // Cost of Goods Sold
        
        for (OrderItem item : items) {
            // Revenue is what the customer paid
            totalRevenue += item.getQuantity() * item.getUnitPriceAtTimeOfSale();
            
            // Cost is what you paid the supplier
            totalCogs += item.getQuantity() * item.getSupplierCostAtTimeOfSale();
        }
        
        double grossProfit = totalRevenue - totalCogs;
        double profitMargin = (totalRevenue == 0) ? 0 : (grossProfit / totalRevenue) * 100;
        
        return new ProfitabilityReport(totalRevenue, totalCogs, grossProfit, profitMargin);
    }
}

