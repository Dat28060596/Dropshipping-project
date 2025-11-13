package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "http://localhost:3000")
public class SupplierController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome Supplier!");
        response.put("features", new String[]{
                "Manage Products",
                "View Orders",
                "Track Shipments",
                "View Payments"
        });
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(Map.of(
                "message", "Products retrieved successfully",
                "count", 0
        ));
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> product) {
        return ResponseEntity.ok(Map.of(
                "message", "Product added successfully",
                "product", product
        ));
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Map<String, Object> product) {
        return ResponseEntity.ok(Map.of(
                "message", "Product updated successfully",
                "productId", productId
        ));
    }
}
