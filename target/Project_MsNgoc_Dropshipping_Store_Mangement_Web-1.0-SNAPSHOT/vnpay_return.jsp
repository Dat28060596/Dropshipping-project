<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.vnpay.config.Config" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.onlinestore.dao.PaymentDAO" %>
<%@ page import="com.onlinestore.model.Payment" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment Result - VNPay</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style>
        .payment-result {
            margin-top: 50px;
            padding: 30px;
            border-radius: 10px;
        }
        .success-icon {
            font-size: 64px;
            color: #28a745;
        }
        .error-icon {
            font-size: 64px;
            color: #dc3545;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                
<%
    // Get all parameters from VNPAY callback
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
    
    // Verify signature
    String signValue = Config.hashAllFields(fields);
    
    if (signValue.equals(vnp_SecureHash)) {
        // Signature is valid
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        String vnpAmount = request.getParameter("vnp_Amount");
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String vnpTransactionNo = request.getParameter("vnp_TransactionNo");
        String vnpBankCode = request.getParameter("vnp_BankCode");
        String vnpCardType = request.getParameter("vnp_CardType");
        String vnpPayDate = request.getParameter("vnp_PayDate");
        String vnpOrderInfo = request.getParameter("vnp_OrderInfo");
        
        // Update database
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
                payment.setVnpTransactionNo(vnpTransactionNo);
                payment.setVnpResponseCode(vnpResponseCode);
                payment.setVnpBankCode(vnpBankCode);
                payment.setVnpCardType(vnpCardType);
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date payDate = dateFormat.parse(vnpPayDate);
                payment.setVnpPayDate(new Timestamp(payDate.getTime()));
                
                if ("00".equals(vnpResponseCode)) {
                    payment.setPaymentStatus("SUCCESS");
                } else {
                    payment.setPaymentStatus("FAILED");
                }
                
                paymentDAO.updatePaymentStatus(vnpTxnRef, payment);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
        
        // Display result
        if ("00".equals(vnpResponseCode)) {
%>
                <div class="payment-result alert alert-success text-center">
                    <div class="success-icon">✓</div>
                    <h2>Payment Successful!</h2>
                    <hr>
                    <div class="text-left">
                        <p><strong>Transaction Number:</strong> <%= vnpTransactionNo %></p>
                        <p><strong>Order Reference:</strong> <%= vnpTxnRef %></p>
                        <p><strong>Amount:</strong> <%= String.format("%,d", Long.parseLong(vnpAmount) / 100) %> VND</p>
                        <p><strong>Bank:</strong> <%= vnpBankCode %></p>
                        <p><strong>Card Type:</strong> <%= vnpCardType != null ? vnpCardType : "N/A" %></p>
                        <p><strong>Description:</strong> <%= vnpOrderInfo %></p>
                        <p><strong>Payment Date:</strong> <%= vnpPayDate %></p>
                    </div>
                    <hr>
                    <a href="dashboard.jsp" class="btn btn-primary btn-lg">Go to Dashboard</a>
                    <a href="payment.jsp" class="btn btn-default btn-lg">Make Another Payment</a>
                </div>
<%
        } else {
            // Payment failed
            String errorMessage = "Unknown error";
            switch(vnpResponseCode) {
                case "07": errorMessage = "Transaction suspicious (related to fraud, unusual transaction)."; break;
                case "09": errorMessage = "Card not registered for Internet Banking."; break;
                case "10": errorMessage = "Customer authentication information is incorrect more than 3 times."; break;
                case "11": errorMessage = "Payment timeout. Please try again."; break;
                case "12": errorMessage = "Card is locked."; break;
                case "13": errorMessage = "Wrong transaction password."; break;
                case "24": errorMessage = "Transaction cancelled."; break;
                case "51": errorMessage = "Insufficient account balance."; break;
                case "65": errorMessage = "Transaction limit exceeded."; break;
                case "75": errorMessage = "Payment bank is under maintenance."; break;
                case "79": errorMessage = "Transaction password entered incorrectly exceeded allowed limit."; break;
                default: errorMessage = "Transaction failed (Code: " + vnpResponseCode + ")";
            }
%>
                <div class="payment-result alert alert-danger text-center">
                    <div class="error-icon">✕</div>
                    <h2>Payment Failed</h2>
                    <hr>
                    <div class="text-left">
                        <p><strong>Error:</strong> <%= errorMessage %></p>
                        <p><strong>Transaction Reference:</strong> <%= vnpTxnRef %></p>
                        <p><strong>Amount:</strong> <%= String.format("%,d", Long.parseLong(vnpAmount) / 100) %> VND</p>
                        <p><strong>Response Code:</strong> <%= vnpResponseCode %></p>
                    </div>
                    <hr>
                    <a href="payment.jsp" class="btn btn-warning btn-lg">Try Again</a>
                    <a href="dashboard.jsp" class="btn btn-default btn-lg">Go to Dashboard</a>
                </div>
<%
        }
    } else {
%>
                <div class="payment-result alert alert-danger text-center">
                    <div class="error-icon">⚠</div>
                    <h2>Security Error</h2>
                    <p>Invalid signature. Payment verification failed.</p>
                    <p>Please contact support if you were charged.</p>
                    <hr>
                    <a href="dashboard.jsp" class="btn btn-default btn-lg">Go to Dashboard</a>
                </div>
<%
    }
%>
                
            </div>
        </div>
    </div>
</body>
</html>
