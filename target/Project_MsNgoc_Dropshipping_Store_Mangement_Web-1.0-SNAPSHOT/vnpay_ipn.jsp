<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.vnpay.config.Config" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.onlinestore.dao.PaymentDAO" %>
<%@ page import="com.onlinestore.model.Payment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // This is the IPN (Instant Payment Notification) endpoint
    // VNPAY calls this URL server-to-server to confirm payment
    
    Map<String, String> fields = new HashMap<>();
    for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
        String fieldName = params.nextElement();
        String fieldValue = request.getParameter(fieldName);
        if (fieldValue != null && fieldValue.length() > 0) {
            fields.put(fieldName, fieldValue);
        }
    }
    
    String vnp_SecureHash = request.getParameter("vnp_SecureHash");
    fields.remove("vnp_SecureHashType");
    fields.remove("vnp_SecureHash");
    
    String signValue = Config.hashAllFields(fields);
    
    String returnCode = "97"; // Default: Checksum failed
    String message = "Invalid Checksum";
    
    if (signValue.equals(vnp_SecureHash)) {
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String vnpAmount = request.getParameter("vnp_Amount");
        
        Connection conn = null;
        try {
            String jdbcURL = application.getInitParameter("jdbcURL");
            String jdbcUsername = application.getInitParameter("jdbcUsername");
            String jdbcPassword = application.getInitParameter("jdbcPassword");
            
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            
            Payment payment = paymentDAO.getPaymentByTxnRef(vnpTxnRef);
            
            if (payment != null) {
                // Check if amount matches
                if (payment.getVnpAmount() != Long.parseLong(vnpAmount)) {
                    returnCode = "04"; // Invalid amount
                    message = "Invalid Amount";
                } else if ("PENDING".equals(payment.getPaymentStatus())) {
                    // Update payment
                    payment.setVnpTransactionNo(request.getParameter("vnp_TransactionNo"));
                    payment.setVnpResponseCode(vnpResponseCode);
                    payment.setVnpBankCode(request.getParameter("vnp_BankCode"));
                    payment.setVnpCardType(request.getParameter("vnp_CardType"));
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    java.util.Date payDate = dateFormat.parse(request.getParameter("vnp_PayDate"));
                    payment.setVnpPayDate(new Timestamp(payDate.getTime()));

                    
                    if ("00".equals(vnpResponseCode)) {
                        payment.setPaymentStatus("SUCCESS");
                    } else {
                        payment.setPaymentStatus("FAILED");
                    }
                    
                    paymentDAO.updatePaymentStatus(vnpTxnRef, payment);
                    returnCode = "00"; // Success
                    message = "Confirm Success";
                } else {
                    returnCode = "02"; // Order already confirmed
                    message = "Order Already Confirmed";
                }
            } else {
                returnCode = "01"; // Order not found
                message = "Order Not Found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnCode = "99"; // Unknown error
            message = "System Error: " + e.getMessage();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
    
    // Return JSON response to VNPAY
    response.setContentType("application/json");
    out.print("{\"RspCode\":\"" + returnCode + "\",\"Message\":\"" + message + "\"}");
%>
