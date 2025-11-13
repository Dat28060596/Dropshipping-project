package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

/**
 * Repository for Order entity
 * Provides database operations for orders
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders within a date range
     * Used for analytics reports (e.g., sales by day, profitability)
     */
    List<Order> findAllByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find orders by status (e.g., "PENDING", "SHIPPED", "DELIVERED")
     */
    List<Order> findAllByStatus(String status);

    /**
     * Find orders by order number
     */
    List<Order> findByOrderNumber(String orderNumber);
}
