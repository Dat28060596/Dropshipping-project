package com.security.filter;

import java.io.IOException;

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
 * CORS Filter for React Frontend Integration
 * Enables cross-origin requests from React app on localhost:3000
 * Allows React to communicate with Servlet API on localhost:8080
 */
@WebFilter(urlPatterns = {"/api/*"})
public class CorsFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization logic (if needed)
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Allow requests from React frontend
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        
        // Allow common HTTP methods
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        
        // Allow common headers
        httpResponse.setHeader("Access-Control-Allow-Headers", 
            "Content-Type, Authorization, X-Requested-With, Accept");
        
        // Allow credentials (cookies, authentication headers)
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Cache preflight requests for 1 hour
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        
        // Handle preflight requests (OPTIONS method)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Filter cleanup logic (if needed)
    }
}
