package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OrderItem;

/**
 * Repository for OrderItem entity
 * Provides database operations for order items
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find all order items for orders within a date range
     * Used for profitability calculations
     */
    List<OrderItem> findAllByOrder_OrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all items for a specific order
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Find items by product name
     */
    List<OrderItem> findByProductName(String productName);
}
