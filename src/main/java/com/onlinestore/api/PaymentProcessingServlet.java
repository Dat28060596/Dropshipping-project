package com.onlinestore.api;

import com.onlinestore.dao.PaymentDAO;
import com.onlinestore.model.Payment;
import com.vnpay.config.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;


public class PaymentProcessingServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. GET USER INFO FROM SESSION/JWT
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            
            if (userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
                return;
            }
            
            // 2. GET PAYMENT PARAMETERS FROM REQUEST
            String orderId = request.getParameter("orderId");
            String amountStr = request.getParameter("amount");
            String orderInfo = request.getParameter("orderInfo");
            String bankCode = request.getParameter("bankCode"); // Optional
            
            // 3. VALIDATE INPUT
            if (orderId == null || amountStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }
            
            long amount = Long.parseLong(amountStr) * 100; // Convert to smallest unit (VND)
            
            if (amount < 10000) { // Minimum 100 VND
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount too small");
                return;
            }
            
            // 4. GENERATE UNIQUE TRANSACTION REFERENCE
            String vnp_TxnRef = Config.getRandomNumber(8);
            
            // 5. SAVE PAYMENT TO DATABASE (STATUS: PENDING)
            Connection conn = null;
            try {
                String jdbcURL = getServletContext().getInitParameter("jdbcURL");
                String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
                String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
                
                conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
                PaymentDAO paymentDAO = new PaymentDAO(conn);
                
                // Check if payment already exists for this order
                Payment existingPayment = paymentDAO.getPaymentByOrderId(orderId);
                if (existingPayment != null && "SUCCESS".equals(existingPayment.getPaymentStatus())) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, "Order already paid");
                    return;
                }
                
                // Create new payment record
                Payment payment = new Payment();
                payment.setOrderId(orderId);
                payment.setUserId(String.valueOf(userId));
                payment.setVnpTxnRef(vnp_TxnRef);
                payment.setVnpAmount(amount);
                payment.setVnpOrderInfo(orderInfo != null ? orderInfo : "Payment for order " + orderId);
                payment.setPaymentStatus("PENDING");
                payment.setIpAddress(getClientIpAddress(request));
                
                int paymentId = paymentDAO.createPayment(payment);
                
                if (paymentId <= 0) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                     "Failed to create payment record");
                    return;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                 "Database error: " + e.getMessage());
                return;
            } finally {
                if (conn != null) try { conn.close(); } catch (Exception e) {}
            }
            
            // 6. BUILD VNPAY PAYMENT URL
            String vnpayUrl = buildVNPayUrl(request, vnp_TxnRef, amount, orderId, 
                                           orderInfo, bankCode);
            
            // 7. REDIRECT TO VNPAY
            response.sendRedirect(vnpayUrl);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount format");
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Payment processing error: " + e.getMessage());
        }
    }
    
    /**
     * Build VNPAY payment URL with all required parameters
     */
    private String buildVNPayUrl(HttpServletRequest request, String vnp_TxnRef, 
                                 long amount, String orderId, String orderInfo, 
                                 String bankCode) throws UnsupportedEncodingException {
        
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = Config.vnp_TmnCode; 
        String vnp_CurrCode = "VND";
        String vnp_Locale = "vn";
        String vnp_OrderType = "other";
        
        // Get return URL from config
        String vnp_ReturnUrl = Config.vnp_ReturnUrl; 
        String vnp_IpnUrl = Config.vnp_IpnUrl;       
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", getClientIpAddress(request));
        
        // Create date format
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15); // Payment expires in 15 minutes
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        // Build query string
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        
        return Config.vnp_PayUrl + "?" + queryUrl;
    }
    
    /**
     * Get client IP address (handles proxy situations)
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
