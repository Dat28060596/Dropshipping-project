/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onlinestore.dao;

/**
 *
 * @author Admin
 */
import com.onlinestore.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection connection;
    
    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, user_id, vnp_txn_ref, vnp_amount, " +
                    "vnp_order_info, payment_status, ip_address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING payment_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getOrderId());
            if (payment.getUserId() != null) {
                stmt.setInt(2, payment.getUserId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, payment.getVnpTxnRef());
            stmt.setLong(4, payment.getVnpAmount());
            stmt.setString(5, payment.getVnpOrderInfo());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getIpAddress());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    public boolean updatePaymentStatus(String vnpTxnRef, Payment payment) throws SQLException {
        String sql = "UPDATE payments SET " +
                    "vnp_transaction_no = ?, vnp_response_code = ?, payment_status = ?, " +
                    "vnp_bank_code = ?, vnp_card_type = ?, vnp_pay_date = ?, " +
                    "updated_at = CURRENT_TIMESTAMP " +
                    "WHERE vnp_txn_ref = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getVnpTransactionNo());
            stmt.setString(2, payment.getVnpResponseCode());
            stmt.setString(3, payment.getPaymentStatus());
            stmt.setString(4, payment.getVnpBankCode());
            stmt.setString(5, payment.getVnpCardType());
            stmt.setTimestamp(6, payment.getVnpPayDate());
            stmt.setString(7, vnpTxnRef);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public Payment getPaymentByTxnRef(String vnpTxnRef) throws SQLException {
        String sql = "SELECT * FROM payments WHERE vnp_txn_ref = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vnpTxnRef);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        }
        return null;
    }
    
    public Payment getPaymentByOrderId(String orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ? " +
                    "ORDER BY created_at DESC LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<Payment> getPaymentsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE user_id = ? ORDER BY created_at DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        }
        return payments;
    }
    
    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getString("order_id"));
        payment.setUserId((Integer) rs.getObject("user_id"));
        payment.setVnpTxnRef(rs.getString("vnp_txn_ref"));
        payment.setVnpTransactionNo(rs.getString("vnp_transaction_no"));
        payment.setVnpAmount(rs.getLong("vnp_amount"));
        payment.setVnpBankCode(rs.getString("vnp_bank_code"));
        payment.setVnpCardType(rs.getString("vnp_card_type"));
        payment.setVnpResponseCode(rs.getString("vnp_response_code"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setVnpPayDate(rs.getTimestamp("vnp_pay_date"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setUpdatedAt(rs.getTimestamp("updated_at"));
        payment.setVnpOrderInfo(rs.getString("vnp_order_info"));
        payment.setIpAddress(rs.getString("ip_address"));
        return payment;
    }
}
