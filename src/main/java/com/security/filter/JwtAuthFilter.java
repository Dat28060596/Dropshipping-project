package com.security.filter;

import java.io.IOException;
import java.util.List;

import com.security.util.JwtUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Authentication Filter
 * Intercepts requests to protected endpoints and validates JWT tokens
 * Attaches user information to request attributes for use in servlets
 */
@WebFilter(urlPatterns = {"/api/orders/*", "/api/admin/*", "/api/staff/*", "/api/supplier/*"})
public class JwtAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization logic (if needed)
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get Authorization header
        String authHeader = httpRequest.getHeader("Authorization");
        
        // Check if Authorization header exists and is valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }
        
        // Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);
        
        // Validate token
        if (!JwtUtil.validateToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return;
        }
        
        // Extract username and roles from token
        String username = JwtUtil.extractUsername(token);
        List<String> roles = JwtUtil.extractRoles(token);
        
        // Attach user information to request attributes
        httpRequest.setAttribute("username", username);
        httpRequest.setAttribute("roles", roles);
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Filter cleanup logic (if needed)
    }
}
