package com.onlinestore.dao;

import com.onlinestore.model.Payment;
import java.sql.*;

public class PaymentDAO {
    private Connection connection;
    
    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Create a new payment record and return the generated ID
     */
    public int createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, user_id, vnp_txn_ref, vnp_amount, " +
                     "vnp_order_info, payment_status, ip_address, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getOrderId());
            stmt.setString(2, payment.getUserId());
            stmt.setString(3, payment.getVnpTxnRef());
            stmt.setLong(4, payment.getVnpAmount());
            stmt.setString(5, payment.getVnpOrderInfo());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getIpAddress());
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }
    
    /**
     * Insert a new payment record (alternative method)
     */
    public void insertPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, user_id, vnp_txn_ref, vnp_amount, " +
                     "vnp_order_info, payment_status, ip_address, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getOrderId());
            stmt.setString(2, payment.getUserId());
            stmt.setString(3, payment.getVnpTxnRef());
            stmt.setLong(4, payment.getVnpAmount());
            stmt.setString(5, payment.getVnpOrderInfo());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getIpAddress());
            stmt.setTimestamp(8, payment.getCreatedDate() != null ? 
                payment.getCreatedDate() : new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }
    
    /**
     * Get payment by transaction reference (vnp_TxnRef)
     */
    public Payment getPaymentByTxnRef(String txnRef) throws SQLException {
        String sql = "SELECT * FROM payments WHERE vnp_txn_ref = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, txnRef);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        }
        return null;
    }
    
    /**
     * Get payment by order ID
     */
    public Payment getPaymentByOrderId(String orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY created_date DESC LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        }
        return null;
    }
    
    /**
     * Update payment status with VNPay response details
     */
    public void updatePaymentStatus(String txnRef, Payment payment) throws SQLException {
        String sql = "UPDATE payments SET vnp_transaction_no = ?, vnp_response_code = ?, " +
                     "vnp_bank_code = ?, vnp_card_type = ?, vnp_pay_date = ?, payment_status = ? " +
                     "WHERE vnp_txn_ref = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getVnpTransactionNo());
            stmt.setString(2, payment.getVnpResponseCode());
            stmt.setString(3, payment.getVnpBankCode());
            stmt.setString(4, payment.getVnpCardType());
            stmt.setTimestamp(5, payment.getVnpPayDate());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, txnRef);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setOrderId(rs.getString("order_id"));
        payment.setUserId(rs.getString("user_id"));
        payment.setVnpTxnRef(rs.getString("vnp_txn_ref"));
        payment.setVnpAmount(rs.getLong("vnp_amount"));
        payment.setVnpOrderInfo(rs.getString("vnp_order_info"));
        payment.setVnpTransactionNo(rs.getString("vnp_transaction_no"));
        payment.setVnpResponseCode(rs.getString("vnp_response_code"));
        payment.setVnpBankCode(rs.getString("vnp_bank_code"));
        payment.setVnpCardType(rs.getString("vnp_card_type"));
        payment.setVnpPayDate(rs.getTimestamp("vnp_pay_date"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setIpAddress(rs.getString("ip_address"));
        payment.setCreatedDate(rs.getTimestamp("created_date"));
        return payment;
    }
}
