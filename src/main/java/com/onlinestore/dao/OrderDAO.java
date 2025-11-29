package com.onlinestore.dao;

import com.onlinestore.model.Order;

import com.onlinestore.model.CartItem;
import com.onlinestore.util.DBConnection;

import java.sql.*;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order, int productId, int quantity) {
        String checkStockSQL = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        String insertOrderSQL = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?) RETURNING id";
        String insertItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement stmt = conn.prepareStatement(checkStockSQL)) {
                stmt.setInt(1, quantity); 
                stmt.setInt(2, productId); 
                stmt.setInt(3, quantity); 
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated == 0) {
                    conn.rollback();
                    return -2; 
                }
            }

            int orderId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSQL)) {
                stmt.setInt(1, order.getUserId());
                stmt.setDouble(2, order.getTotalPrice());
                stmt.setString(3, order.getStatus());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

            if (orderId > 0) {
                try (PreparedStatement stmt = conn.prepareStatement(insertItemSQL)) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, productId);
                    stmt.setInt(3, quantity);
                    stmt.setDouble(4, order.getTotalPrice());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1; 
        }
    }

   

    public boolean updateStatus(int orderId, int supplierId, String newStatus) {
        String sql = "UPDATE order_items SET status = ? " +
                     "FROM products " +
                     "WHERE order_items.product_id = products.id " +
                     "AND order_items.order_id = ? " +
                     "AND products.supplier_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.setInt(3, supplierId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    

    public int createOrderFromCart(Long userId, double grandTotal, List<CartItem> cart, String address, String phone) {
        String checkStockSQL = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        String insertOrderSQL = "INSERT INTO orders (user_id, total_price, status, shipping_address, customer_phone) VALUES (?, ?, 'PENDING', ?, ?) RETURNING id";
        String insertItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            int orderId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSQL)) {
                stmt.setLong(1, userId);
                stmt.setDouble(2, grandTotal);
                stmt.setString(3, address);
                stmt.setString(4, phone);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) orderId = rs.getInt(1);
            }

            if (orderId == 0) throw new SQLException("Failed to create order");

            try (PreparedStatement stockStmt = conn.prepareStatement(checkStockSQL);
                PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                
                for (CartItem item : cart) {
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProduct().getId());
                    stockStmt.setInt(3, item.getQuantity());
                    
                    int rows = stockStmt.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Out of stock for: " + item.getProduct().getName());
                    }

                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProduct().getId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getProduct().getPrice());
                    itemStmt.executeUpdate();
                }
            }

            conn.commit(); 
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1;
        }
    }

    public boolean updateAllItemsStatus(int orderId, String newStatus) {
        String sql = "UPDATE order_items SET status = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}