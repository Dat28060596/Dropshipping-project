package com.example.demo;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.enums.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@SpringBootApplication
@RestController
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Health check endpoint - accessible from browser
    @GetMapping("/")
    public String home() {
        return "✅ Spring Boot Backend Running on Port 8081\n\n" +
               "API Documentation:\n" +
               "- POST /api/auth/login - Login endpoint\n" +
               "- POST /api/auth/register - Register endpoint\n" +
               "- GET /api/auth/me - Current user (protected)\n" +
               "- GET /api/admin/dashboard - Admin only\n" +
               "- GET /api/staff/dashboard - Staff only\n" +
               "- GET /api/supplier/dashboard - Supplier only\n\n" +
               "Test Users:\n" +
               "- admin / admin123 (ADMIN role)\n" +
               "- staff / staff123 (STAFF role)\n" +
               "- supplier / supplier123 (SUPPLIER role)\n" +
               "- user / user123 (USER role)";
    }

    @GetMapping("/health")
    public String health() {
        return "✅ Backend is Healthy";
    }

    // CORS configuration for React frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    // Seed initial users with different roles
    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setFullName("Admin User");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ADMIN));
                admin.setEnabled(true);
                userRepository.save(admin);
            }

            // Create staff user
            if (!userRepository.existsByUsername("staff")) {
                User staff = new User();
                staff.setUsername("staff");
                staff.setEmail("staff@example.com");
                staff.setFullName("Staff User");
                staff.setPassword(passwordEncoder.encode("staff123"));
                staff.setRoles(Set.of(Role.STAFF));
                staff.setEnabled(true);
                userRepository.save(staff);
            }

            // Create supplier user
            if (!userRepository.existsByUsername("supplier")) {
                User supplier = new User();
                supplier.setUsername("supplier");
                supplier.setEmail("supplier@example.com");
                supplier.setFullName("Supplier User");
                supplier.setPassword(passwordEncoder.encode("supplier123"));
                supplier.setRoles(Set.of(Role.SUPPLIER));
                supplier.setEnabled(true);
                userRepository.save(supplier);
            }

            // Create regular user
            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setFullName("Regular User");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(Role.USER));
                user.setEnabled(true);
                userRepository.save(user);
            }
        };
    }
}

