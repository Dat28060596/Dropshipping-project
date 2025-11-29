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
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            max-width: 600px;
            width: 100%;
            padding: 40px;
        }
        .success-icon {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: #10b981;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 40px;
            color: white;
        }
        .error-icon {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: #ef4444;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 40px;
            color: white;
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #1f2937;
        }
        .success h2 { color: #10b981; }
        .error h2 { color: #ef4444; }
        .info-box {
            background: #f9fafb;
            border-radius: 12px;
            padding: 20px;
            margin: 20px 0;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #e5e7eb;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 600;
            color: #6b7280;
        }
        .info-value {
            color: #1f2937;
            font-weight: 500;
            text-align: right;
        }
        .btn {
            display: inline-block;
            padding: 14px 28px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            text-align: center;
            transition: all 0.3s;
            width: 100%;
            margin-top: 20px;
        }
        .btn-primary {
            background: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .btn-secondary {
            background: #6b7280;
            color: white;
        }
        .btn-secondary:hover {
            background: #4b5563;
        }
    </style>
</head>
<body>
<%
    // Get all parameters from VNPay
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
    
    String vnpTxnRef = request.getParameter("vnp_TxnRef");
    String vnpAmount = request.getParameter("vnp_Amount");
    String vnpResponseCode = request.getParameter("vnp_ResponseCode");
    String vnpTransactionNo = request.getParameter("vnp_TransactionNo");
    String vnpBankCode = request.getParameter("vnp_BankCode");
    String vnpCardType = request.getParameter("vnp_CardType");
    String vnpOrderInfo = request.getParameter("vnp_OrderInfo");
    String vnpPayDate = request.getParameter("vnp_PayDate");
    
    if (signValue.equals(vnp_SecureHash)) {
        Connection conn = null;
        try {
            // Get database connection
            String jdbcURL = application.getInitParameter("jdbcURL");
            String jdbcUsername = application.getInitParameter("jdbcUsername");
            String jdbcPassword = application.getInitParameter("jdbcPassword");
            
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            
            // Try to get existing payment
            Payment payment = paymentDAO.getPaymentByTxnRef(vnpTxnRef);
            
            if (payment == null) {
                // Create new payment record if not exists
                payment = new Payment();
                payment.setVnpTxnRef(vnpTxnRef);
                payment.setVnpAmount(Long.parseLong(vnpAmount));
                payment.setVnpOrderInfo(vnpOrderInfo);
                payment.setPaymentStatus("PENDING");
                payment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                
                paymentDAO.insertPayment(payment);
            }
            
            // Update payment with VNPay response
            if ("PENDING".equals(payment.getPaymentStatus()) || payment.getVnpTransactionNo() == null) {
                payment.setVnpTransactionNo(vnpTransactionNo);
                payment.setVnpResponseCode(vnpResponseCode);
                payment.setVnpBankCode(vnpBankCode);
                payment.setVnpCardType(vnpCardType);
                
                // Parse payment date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                java.util.Date payDate = dateFormat.parse(vnpPayDate);
                payment.setVnpPayDate(new Timestamp(payDate.getTime()));
                
                // Set status based on response code
                if ("00".equals(vnpResponseCode)) {
                    payment.setPaymentStatus("SUCCESS");
                } else {
                    payment.setPaymentStatus("FAILED");
                }
                
                paymentDAO.updatePaymentStatus(vnpTxnRef, payment);
            }
            
            // Display result to user
            if ("00".equals(vnpResponseCode)) {
%>
    <div class="container success">
        <div class="success-icon">✓</div>
        <h2>Payment Successful!</h2>
        <p style="text-align: center; color: #6b7280; margin-bottom: 20px;">
            Your payment has been processed successfully
        </p>
        
        <div class="info-box">
            <div class="info-row">
                <span class="info-label">Transaction Number:</span>
                <span class="info-value"><%= vnpTransactionNo %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Order Reference:</span>
                <span class="info-value"><%= vnpTxnRef %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Amount:</span>
                <span class="info-value"><%= String.format("%,d", Long.parseLong(vnpAmount) / 100) %> VND</span>
            </div>
            <div class="info-row">
                <span class="info-label">Bank:</span>
                <span class="info-value"><%= vnpBankCode %></span>
            </div>
            <% if (vnpCardType != null && !vnpCardType.isEmpty()) { %>
            <div class="info-row">
                <span class="info-label">Card Type:</span>
                <span class="info-value"><%= vnpCardType %></span>
            </div>
            <% } %>
            <div class="info-row">
                <span class="info-label">Payment Date:</span>
                <span class="info-value"><%= vnpPayDate.substring(6, 8) + "/" + 
                    vnpPayDate.substring(4, 6) + "/" + vnpPayDate.substring(0, 4) + " " +
                    vnpPayDate.substring(8, 10) + ":" + vnpPayDate.substring(10, 12) %></span>
            </div>
        </div>
        
        <a href="index.jsp" class="btn btn-primary">Return to Home</a>
    </div>
<%
            } else {
                // Map common error codes
                String errorMessage;
                switch(vnpResponseCode) {
                    case "07": errorMessage = "Transaction suspected of fraud"; break;
                    case "09": errorMessage = "Card/Account not registered for Internet Banking"; break;
                    case "10": errorMessage = "Incorrect card/account authentication"; break;
                    case "11": errorMessage = "Payment timeout"; break;
                    case "12": errorMessage = "Card/Account locked"; break;
                    case "13": errorMessage = "Incorrect OTP"; break;
                    case "24": errorMessage = "Transaction cancelled"; break;
                    case "51": errorMessage = "Insufficient balance"; break;
                    case "65": errorMessage = "Account exceeded daily transaction limit"; break;
                    case "75": errorMessage = "Payment bank is under maintenance"; break;
                    case "79": errorMessage = "Transaction failed (exceeded number of OTP entries)"; break;
                    default: errorMessage = "Payment failed (Code: " + vnpResponseCode + ")";
                }
%>
    <div class="container error">
        <div class="error-icon">✗</div>
        <h2>Payment Failed</h2>
        <p style="text-align: center; color: #6b7280; margin-bottom: 20px;">
            <%= errorMessage %>
        </p>
        
        <div class="info-box">
            <div class="info-row">
                <span class="info-label">Transaction Reference:</span>
                <span class="info-value"><%= vnpTxnRef %></span>
            </div>
            <div class="info-row">
                <span class="info-label">Amount:</span>
                <span class="info-value"><%= String.format("%,d", Long.parseLong(vnpAmount) / 100) %> VND</span>
            </div>
            <div class="info-row">
                <span class="info-label">Response Code:</span>
                <span class="info-value"><%= vnpResponseCode %></span>
            </div>
        </div>
        
        <a href="vnpay_checkout.jsp" class="btn btn-primary">Try Again</a>
        <a href="index.jsp" class="btn btn-secondary">Return to Home</a>
    </div>
<%
            }
        } catch (Exception e) {
            e.printStackTrace();
%>
    <div class="container error">
        <div class="error-icon">⚠</div>
        <h2>System Error</h2>
        <p style="text-align: center; color: #6b7280; margin-bottom: 20px;">
            An error occurred while processing your payment: <%= e.getMessage() %>
        </p>
        <a href="index.jsp" class="btn btn-secondary">Return to Home</a>
    </div>
<%
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    } else {
%>
    <div class="container error">
        <div class="error-icon">⚠</div>
        <h2>Invalid Signature</h2>
        <p style="text-align: center; color: #6b7280; margin-bottom: 20px;">
            Payment verification failed. Please contact support if you were charged.
        </p>
        <a href="index.jsp" class="btn btn-secondary">Return to Home</a>
    </div>
<%
    }
%>
</body>
</html>
