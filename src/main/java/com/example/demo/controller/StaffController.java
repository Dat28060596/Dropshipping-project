package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome Staff!");
        response.put("features", new String[]{
                "View Orders",
                "Process Orders",
                "View Analytics",
                "Generate Reports"
        });
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok(Map.of(
                "message", "Orders retrieved successfully",
                "count", 0
        ));
    }

    @PostMapping("/orders/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(Map.of(
                "message", "Order status updated",
                "orderId", orderId,
                "status", status
        ));
    }
}
