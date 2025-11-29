<%-- 
    Centralized API Configuration for JSP Pages
    This file centralizes all API endpoint URLs to avoid hardcoding them across multiple JSP files
    
    Usage in JSP files:
    <%@ include file="config.jsp" %>
    
    Then use variables like:
    - API_AUTH_LOGIN
    - API_AUTH_REGISTER
    - API_ORDERS
    - API_ANALYTICS
    - API_PAYMENT_PROCESS
    etc.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Get context path
    String contextPath = request.getContextPath();
    
    // ==================== API ENDPOINTS ====================
    // Authentication Endpoints
    String API_AUTH_LOGIN = contextPath + "/api/auth/login";
    String API_AUTH_REGISTER = contextPath + "/api/auth/register";
    String API_AUTH_LOGOUT = contextPath + "/api/auth/logout";
    
    
    // Analytics Endpoints
    String API_ANALYTICS = contextPath + "/api/analytics";
    String API_ANALYTICS_EXPORT_PDF = contextPath + "/api/analytics/export-pdf";
    
    // Payment Endpoints
    String API_PAYMENT_PROCESS = contextPath + "/api/payment/process";
    String API_VNPAY_RETURN = contextPath + "/api/vnpay_return.jsp";
    String API_VNPAY_IPN = contextPath + "/api/vnpay_ipn.jsp";
    
    // ==================== NAVIGATION LINKS ====================
    String LINK_DASHBOARD = contextPath + "/dashboard.jsp";
    String LINK_LOGIN = contextPath + "/Login";
    String LINK_REGISTER = contextPath + "/register.jsp";
    String LINK_LOGOUT = contextPath + "/Logout";
    String LINK_PAYMENT = contextPath + "/payment.jsp";
    String LINK_ANALYTICS = contextPath + "/analytics.jsp";
    String LINK_LANDING = contextPath + "/landing.jsp";
    String LINK_PRODUCTS = contextPath + "/products.jsp";
    String LINK_SETTINGS = contextPath + "/settings.jsp";
    String LINK_ORDERS = contextPath + "/orders.jsp";
    
    // ==================== ENVIRONMENT VARIABLES ====================
    String appEnv = System.getenv("APP_ENV") != null ? System.getenv("APP_ENV") : "development";
    String frontendUrl = System.getenv("FRONTEND_URL") != null ? System.getenv("FRONTEND_URL") : "http://localhost:3000";
    String baseUrl = System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : "http://localhost:8080";
    
    // Make variables available globally in JSP pages that include this file
    request.setAttribute("contextPath", contextPath);
    request.setAttribute("appEnv", appEnv);
    request.setAttribute("API_AUTH_LOGIN", API_AUTH_LOGIN);
    request.setAttribute("API_AUTH_REGISTER", API_AUTH_REGISTER);
    request.setAttribute("API_AUTH_LOGOUT", API_AUTH_LOGOUT);
    
    request.setAttribute("API_ANALYTICS", API_ANALYTICS);
    request.setAttribute("API_ANALYTICS_EXPORT_PDF", API_ANALYTICS_EXPORT_PDF);
    request.setAttribute("API_PAYMENT_PROCESS", API_PAYMENT_PROCESS);
    request.setAttribute("LINK_DASHBOARD", LINK_DASHBOARD);
    request.setAttribute("LINK_LOGIN", LINK_LOGIN);
    request.setAttribute("LINK_LOGOUT", LINK_LOGOUT);
    ;
    
%>
