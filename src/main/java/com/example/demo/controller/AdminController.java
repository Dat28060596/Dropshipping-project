package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome Admin!");
        response.put("features", new String[]{
                "Manage Users",
                "View Reports",
                "System Settings",
                "Manage Roles"
        });
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRole(@RequestParam String username, @RequestParam String role) {
        return ResponseEntity.ok(Map.of(
                "message", "Role assigned successfully",
                "username", username,
                "role", role
        ));
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of(
                "message", "User deleted successfully",
                "userId", userId
        ));
    }
}
