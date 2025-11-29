package com.vnpay.api;

import com.vnpay.config.Config;
import com.vnpay.util.VNPayUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // Lấy tất cả parameters từ VNPay trả về
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }
        
        // Lấy vnp_SecureHash từ response
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        
        // Xóa các tham số không cần thiết khỏi danh sách để tính hash
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        
        // Tính toán secure hash để verify
        String signValue = VNPayUtil.hashAllFields(fields, Config.secretKey);
        
        // Lấy các thông tin từ response
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_PayDate = request.getParameter("vnp_PayDate");
        
        // Chuyển đổi số tiền (VNPay trả về số tiền x100)
        long amount = 0;
        if (vnp_Amount != null) {
            amount = Long.parseLong(vnp_Amount) / 100;
        }
        
        // Kiểm tra chữ ký có hợp lệ không
        if (signValue.equals(vnp_SecureHash)) {
            // Chữ ký hợp lệ
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                request.setAttribute("status", "success");
                request.setAttribute("message", "Giao dịch thành công!");
                request.setAttribute("orderInfo", vnp_OrderInfo);
                request.setAttribute("amount", amount);
                request.setAttribute("transactionNo", vnp_TransactionNo);
                request.setAttribute("txnRef", vnp_TxnRef);
                request.setAttribute("bankCode", vnp_BankCode);
                request.setAttribute("payDate", vnp_PayDate);
                
                // TODO: Cập nhật trạng thái đơn hàng trong database
                // updateOrderStatus(vnp_TxnRef, "PAID", vnp_TransactionNo);
                
            } else {
                // Giao dịch thất bại
                request.setAttribute("status", "failed");
                request.setAttribute("message", getResponseMessage(vnp_ResponseCode));
                request.setAttribute("orderInfo", vnp_OrderInfo);
                request.setAttribute("amount", amount);
                request.setAttribute("txnRef", vnp_TxnRef);
                request.setAttribute("responseCode", vnp_ResponseCode);
                
                // TODO: Cập nhật trạng thái đơn hàng trong database
                // updateOrderStatus(vnp_TxnRef, "FAILED", vnp_ResponseCode);
            }
        } else {
            // Chữ ký không hợp lệ
            request.setAttribute("status", "error");
            request.setAttribute("message", "Chữ ký không hợp lệ. Giao dịch có thể bị giả mạo!");
            
            // Log lại để kiểm tra
            System.out.println("Invalid signature!");
            System.out.println("Expected: " + signValue);
            System.out.println("Received: " + vnp_SecureHash);
        }
        
        // Forward đến trang kết quả
        request.getRequestDispatcher("/WEB-INF/views/vnpay-result.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Lấy message tương ứng với mã response code từ VNPay
     */
    private String getResponseMessage(String responseCode) {
        switch (responseCode) {
            case "00":
                return "Giao dịch thành công";
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10":
                return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13":
                return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51":
                return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65":
                return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì.";
            case "79":
                return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
            default:
                return "Giao dịch thất bại. Mã lỗi: " + responseCode;
        }
    }
    
    /**
     * TODO: Implement method to update order status in database
     */
    private void updateOrderStatus(String orderId, String status, String transactionNo) {
        // Implement your database update logic here
        // Example:
        // OrderDAO orderDAO = new OrderDAO();
        // orderDAO.updateStatus(orderId, status, transactionNo);
    }
}
