package com.config;

/**
 * Centralized Application Configuration
 * Manages all API endpoints, CORS settings, database connections, and external service URLs
 * 
 * Usage:
 *   - String baseUrl = AppConfig.getBaseUrl();
 *   - String authEndpoint = AppConfig.getAuthLoginEndpoint();
 *   - String corsOrigin = AppConfig.getCorsOrigin();
 */
public class AppConfig {

    // ==================== ENVIRONMENT ====================
    private static final String ENV = System.getenv("APP_ENV") != null ? System.getenv("APP_ENV") : "development";
    private static final boolean isDevelopment = "development".equals(ENV);
    private static final boolean isProduction = "production".equals(ENV);

    // ==================== SERVER CONFIGURATION ====================
    
    /**
     * Base server URL (protocol + host + port)
     */
    public static String getBaseUrl() {
        if (isProduction) {
            return System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : "https://api.yourdomain.com";
        }
        return "http://localhost:8080";
    }

    /**
     * Context path for the application
     */
    public static String getContextPath() {
        return "";  // e.g., "/dropshipping" if app is at /dropshipping
    }

    // ==================== FRONTEND CONFIGURATION ====================
    
    /**
     * Frontend URL for CORS and redirects
     */
    public static String getFrontendUrl() {
        if (isProduction) {
            return System.getenv("FRONTEND_URL") != null ? System.getenv("FRONTEND_URL") : "https://yourdomain.com";
        }
        return "http://localhost:3000";
    }

    // ==================== CORS SETTINGS ====================
    
    /**
     * Allowed CORS origin for API requests
     */
    public static String getCorsOrigin() {
        return getFrontendUrl();
    }

    /**
     * CORS allowed methods
     */
    public static String getCorsAllowedMethods() {
        return "GET, POST, PUT, DELETE, OPTIONS, PATCH";
    }

    /**
     * CORS allowed headers
     */
    public static String getCorsAllowedHeaders() {
        return "Content-Type, Authorization, X-Requested-With";
    }

    /**
     * CORS allow credentials
     */
    public static boolean isCorsAllowCredentials() {
        return true;
    }

    // ==================== API ENDPOINTS ====================
    
    // Authentication Endpoints
    public static String getAuthLoginEndpoint() {
        return getBaseUrl() + "/api/auth/login";
    }

    public static String getAuthRegisterEndpoint() {
        return getBaseUrl() + "/api/auth/register";
    }

    public static String getAuthLogoutEndpoint() {
        return getBaseUrl() + "/api/auth/logout";
    }

    // Order Endpoints
    public static String getOrdersEndpoint() {
        return getBaseUrl() + "/api/orders";
    }

    public static String getOrderEndpoint(String orderId) {
        return getBaseUrl() + "/api/orders/" + orderId;
    }

    public static String getOrderCreateEndpoint() {
        return getBaseUrl() + "/api/orders";
    }

    public static String getOrderUpdateEndpoint(String orderId) {
        return getBaseUrl() + "/api/orders/" + orderId;
    }

    public static String getOrderDeleteEndpoint(String orderId) {
        return getBaseUrl() + "/api/orders/" + orderId;
    }

    // Product Endpoints
    public static String getProductsEndpoint() {
        return getBaseUrl() + "/api/products";
    }

    public static String getProductEndpoint(String productId) {
        return getBaseUrl() + "/api/products/" + productId;
    }

    // Analytics Endpoints
    public static String getAnalyticsExportPdfEndpoint() {
        return getBaseUrl() + "/api/analytics/export-pdf";
    }

    public static String getAnalyticsEndpoint() {
        return getBaseUrl() + "/api/analytics";
    }

    // Payment Endpoints
    public static String getPaymentProcessEndpoint() {
        return getBaseUrl() + "/api/payment/process";
    }

    public static String getVnPayReturnEndpoint() {
        return getBaseUrl() + "/api/vnpay_return.jsp";
    }

    public static String getVnPayIpnEndpoint() {
        return getBaseUrl() + "/api/vnpay_ipn.jsp";
    }

    // ==================== SERVLET ENDPOINT PATTERNS ====================
    
    /**
     * Servlet URL pattern for authentication
     */
    public static String getAuthServletPattern() {
        return "/api/auth/*";
    }

    /**
     * Servlet URL pattern for orders
     */
    public static String getOrderServletPattern() {
        return "/api/orders/*";
    }

    /**
     * Servlet URL pattern for products
     */
    public static String getProductServletPattern() {
        return "/api/products/*";
    }

    /**
     * Servlet URL pattern for payments
     */
    public static String getPaymentServletPattern() {
        return "/api/payment/*";
    }

    /**
     * Servlet URL pattern for analytics
     */
    public static String getAnalyticsServletPattern() {
        return "/api/analytics/*";
    }

    // ==================== DATABASE CONFIGURATION ====================
    
    /**
     * Database URL (JDBC connection string)
     */
    public static String getDatabaseUrl() {
        return System.getenv("DATABASE_URL") != null 
            ? System.getenv("DATABASE_URL")
            : "jdbc:postgresql://db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres";
    }

    /**
     * Database username
     */
    public static String getDatabaseUser() {
        return System.getenv("DATABASE_USER") != null 
            ? System.getenv("DATABASE_USER")
            : "postgres";
    }

    /**
     * Database password
     */
    public static String getDatabasePassword() {
        return System.getenv("DATABASE_PASSWORD") != null 
            ? System.getenv("DATABASE_PASSWORD")
            : "thisisnotai123";
    }

    /**
     * Database driver class
     */
    public static String getDatabaseDriver() {
        return "org.postgresql.Driver";
    }

    // ==================== EXTERNAL SERVICES ====================
    
    /**
     * VNPay Payment Gateway URL
     */
    public static String getVnPayUrl() {
        return "https://sandbox.vnpayment.vn/paygate/pay.html";
    }

    /**
     * VNPay API URL
     */
    public static String getVnPayApiUrl() {
        return "https://sandbox.vnpayment.vn/merchant_webapi/merchant.do";
    }

    /**
     * VNPay Merchant Code
     */
    public static String getVnPayTmnCode() {
        return System.getenv("VNPAY_TMN_CODE") != null 
            ? System.getenv("VNPAY_TMN_CODE")
            : "2QXVN0OK";
    }

    /**
     * VNPay Hash Secret
     */
    public static String getVnPayHashSecret() {
        return System.getenv("VNPAY_HASH_SECRET") != null 
            ? System.getenv("VNPAY_HASH_SECRET")
            : "SCJVZJASVFJBGFVXFVXBFXBFBFXBFXB";
    }

    // ==================== JWT CONFIGURATION ====================
    
    /**
     * JWT Secret Key
     */
    public static String getJwtSecret() {
        return System.getenv("JWT_SECRET") != null 
            ? System.getenv("JWT_SECRET")
            : "your_jwt_secret_key_change_in_production";
    }

    /**
     * JWT Token Expiration (in hours)
     */
    public static int getJwtExpirationHours() {
        String expiration = System.getenv("JWT_EXPIRATION_HOURS");
        return expiration != null ? Integer.parseInt(expiration) : 24;
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Check if application is in development mode
     */
    public static boolean isDevelopmentMode() {
        return isDevelopment;
    }

    /**
     * Check if application is in production mode
     */
    public static boolean isProductionMode() {
        return isProduction;
    }

    /**
     * Get environment name
     */
    public static String getEnvironment() {
        return ENV;
    }

    /**
     * Get full API URL (base URL + servlet pattern + endpoint)
     */
    public static String buildApiUrl(String servletPattern, String endpoint) {
        return getBaseUrl() + servletPattern.replace("/*", "") + endpoint;
    }
}
