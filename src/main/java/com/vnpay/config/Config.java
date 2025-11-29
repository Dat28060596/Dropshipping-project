package com.vnpay.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Config {
    
    // VNPay URLs
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    
    // Your VNPAY credentials (Already correct!)
    public static String vnp_TmnCode = "VW4CHF8Y";
    public static String secretKey = "FEWXXWKLFFOTOINDLREISFEZVKOFHXBU";
    
    // Return URLs - UPDATE THESE to match your application
    public static String vnp_ReturnUrl = "http://localhost:8080/vnpay_return.jsp";
    public static String vnp_IpnUrl = "http://localhost:8080/vnpay_ipn.jsp";
    
    // API Version
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    
    // ADD THESE HELPER METHODS
    
    /**
     * Generate random transaction reference number
     */
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * Calculate HMAC SHA512 for signature
     */
    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }
    
    /**
     * Hash all fields for signature verification
     */
    public static String hashAllFields(java.util.Map<String, String> fields) {
        java.util.List<String> fieldNames = new java.util.ArrayList<>(fields.keySet());
        java.util.Collections.sort(fieldNames);
        
        StringBuilder sb = new StringBuilder();
        java.util.Iterator<String> itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                sb.append(fieldName);
                sb.append('=');
                sb.append(fieldValue);
                if (itr.hasNext()) {
                    sb.append('&');
                }
            }
        }
        
        return hmacSHA512(secretKey, sb.toString());
    }
}
