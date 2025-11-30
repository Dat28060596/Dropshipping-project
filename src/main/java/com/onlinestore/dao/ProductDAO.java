package com.onlinestore.dao;

import com.onlinestore.model.Product;
import com.onlinestore.model.ProductVariant;
import com.onlinestore.model.StockAlert;
import com.onlinestore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    // ===== Product CRUD =====
    
    public int createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, detailed_description, " +
                    "price, stock, category_id, has_variants, specifications, tags) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?) RETURNING id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getDetailedDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setInt(6, product.getCategoryId());
            ps.setBoolean(7, product.isHasVariants());
            ps.setString(8, product.getSpecifications());
            
            // FIX: Check if tags is not null before creating array
            if (product.getTags() != null && product.getTags().length > 0) {
                Array tagsArray = conn.createArrayOf("TEXT", product.getTags());
                ps.setArray(9, tagsArray);
            } else {
                ps.setNull(9, Types.ARRAY);
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, description=?, detailed_description=?, " +
                    "price=?, stock=?, category_id=?, has_variants=?, specifications=?::jsonb, " +
                    "tags=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getDetailedDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setInt(6, product.getCategoryId());
            ps.setBoolean(7, product.isHasVariants());
            ps.setString(8, product.getSpecifications());
            
            // FIX: Check if tags is not null before creating array
            if (product.getTags() != null && product.getTags().length > 0) {
                Array tagsArray = conn.createArrayOf("TEXT", product.getTags());
                ps.setArray(9, tagsArray);
            } else {
                ps.setNull(9, Types.ARRAY);
            }
            
            ps.setInt(10, product.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                
                // Load variants if product has variants
                if (product.isHasVariants()) {
                    product.setVariants(getProductVariants(id));
                }
                
                return product;
            }
        }
        return null;
    }
    
    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.id DESC";
        
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Product product = mapResultSetToProduct(rs);
                products.add(product);
            }
        }
        return products;
    }
    
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    // ===== Product Variants =====
    
    public int createVariant(ProductVariant variant) throws SQLException {
        String sql = "INSERT INTO product_variants (product_id, sku, size, color, material, " +
                    "price, stock_quantity, low_stock_threshold, image_url, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSku());
            ps.setString(3, variant.getSize());
            ps.setString(4, variant.getColor());
            ps.setString(5, variant.getMaterial());
            ps.setDouble(6, variant.getPrice());
            ps.setInt(7, variant.getStockQuantity());
            ps.setInt(8, variant.getLowStockThreshold());
            ps.setString(9, variant.getImageUrl());
            ps.setBoolean(10, variant.isActive());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int variantId = rs.getInt(1);
                checkAndCreateStockAlert(variant.getProductId(), variantId, 
                                        variant.getStockQuantity(), 
                                        variant.getLowStockThreshold());
                return variantId;
            }
        }
        return -1;
    }
    
    public boolean updateVariant(ProductVariant variant) throws SQLException {
        String sql = "UPDATE product_variants SET sku=?, size=?, color=?, material=?, " +
                    "price=?, stock_quantity=?, low_stock_threshold=?, image_url=?, " +
                    "is_active=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, variant.getSku());
            ps.setString(2, variant.getSize());
            ps.setString(3, variant.getColor());
            ps.setString(4, variant.getMaterial());
            ps.setDouble(5, variant.getPrice());
            ps.setInt(6, variant.getStockQuantity());
            ps.setInt(7, variant.getLowStockThreshold());
            ps.setString(8, variant.getImageUrl());
            ps.setBoolean(9, variant.isActive());
            ps.setInt(10, variant.getId());
            
            boolean updated = ps.executeUpdate() > 0;
            
            if (updated) {
                checkAndCreateStockAlert(variant.getProductId(), variant.getId(), 
                                        variant.getStockQuantity(), 
                                        variant.getLowStockThreshold());
            }
            
            return updated;
        }
    }
    
    public List<ProductVariant> getProductVariants(int productId) throws SQLException {
        String sql = "SELECT * FROM product_variants WHERE product_id = ? AND is_active = true " +
                    "ORDER BY id";
        
        List<ProductVariant> variants = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                variants.add(mapResultSetToVariant(rs));
            }
        }
        return variants;
    }
    
    public ProductVariant getVariantById(int variantId) throws SQLException {
        String sql = "SELECT * FROM product_variants WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVariant(rs);
            }
        }
        return null;
    }
    
    // ===== Inventory Management =====
    
    public boolean updateStock(int productId, Integer variantId, int quantityChange, 
                               String changeType, String reason, int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            int previousStock, newStock;
            
            if (variantId != null) {
                // Update variant stock
                String getStockSql = "SELECT stock_quantity, low_stock_threshold FROM product_variants WHERE id = ?";
                int threshold = 10;
                
                try (PreparedStatement ps = conn.prepareStatement(getStockSql)) {
                    ps.setInt(1, variantId);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    previousStock = rs.getInt("stock_quantity");
                    threshold = rs.getInt("low_stock_threshold");
                }
                
                newStock = previousStock + quantityChange;
                
                String updateSql = "UPDATE product_variants SET stock_quantity = ?, " +
                                  "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, newStock);
                    ps.setInt(2, variantId);
                    ps.executeUpdate();
                }
                
                // Check for low stock alert using the existing connection
                checkAndCreateStockAlert(conn, productId, variantId, newStock, threshold);
                
            } else {
                // Update main product stock
                String getStockSql = "SELECT stock FROM products WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(getStockSql)) {
                    ps.setInt(1, productId);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    previousStock = rs.getInt("stock");
                }
                
                newStock = previousStock + quantityChange;
                
                String updateSql = "UPDATE products SET stock = ?, updated_at = CURRENT_TIMESTAMP " +
                                  "WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, newStock);
                    ps.setInt(2, productId);
                    ps.executeUpdate();
                }
            }
            
            // Log inventory change
            String logSql = "INSERT INTO inventory_logs (product_id, variant_id, change_type, " +
                           "quantity_change, previous_stock, new_stock, reason, performed_by) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(logSql)) {
                ps.setInt(1, productId);
                if (variantId != null) {
                    ps.setInt(2, variantId);
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                ps.setString(3, changeType);
                ps.setInt(4, quantityChange);
                ps.setInt(5, previousStock);
                ps.setInt(6, newStock);
                ps.setString(7, reason);
                ps.setInt(8, userId);
                ps.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public List<StockAlert> getPendingStockAlerts() throws SQLException {
        String sql = "SELECT sa.*, p.name as product_name, " +
                    "COALESCE(pv.size || ' ' || pv.color, '') as variant_info " +
                    "FROM stock_alerts sa " +
                    "JOIN products p ON sa.product_id = p.id " +
                    "LEFT JOIN product_variants pv ON sa.variant_id = pv.id " +
                    "WHERE sa.status = 'PENDING' " +
                    "ORDER BY sa.created_at DESC";
        
        List<StockAlert> alerts = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                alerts.add(mapResultSetToStockAlert(rs));
            }
        }
        return alerts;
    }
    
    // FIX: Added overloaded method that accepts Connection parameter
    private void checkAndCreateStockAlert(Connection conn, int productId, Integer variantId, 
                                         int currentStock, int threshold) throws SQLException {
        if (currentStock <= threshold) {
            String alertType = currentStock == 0 ? "OUT_OF_STOCK" : "LOW_STOCK";
            
            // Check if alert already exists
            String checkSql = "SELECT id FROM stock_alerts WHERE product_id = ? " +
                             "AND (variant_id = ? OR (variant_id IS NULL AND ? IS NULL)) " +
                             "AND status = 'PENDING'";
            
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, productId);
                if (variantId != null) {
                    ps.setInt(2, variantId);
                    ps.setInt(3, variantId);
                } else {
                    ps.setNull(2, Types.INTEGER);
                    ps.setNull(3, Types.INTEGER);
                }
                
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    // Create new alert
                    String insertSql = "INSERT INTO stock_alerts (product_id, variant_id, " +
                                      "alert_type, current_stock, threshold_value) " +
                                      "VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, productId);
                        if (variantId != null) {
                            insertPs.setInt(2, variantId);
                        } else {
                            insertPs.setNull(2, Types.INTEGER);
                        }
                        insertPs.setString(3, alertType);
                        insertPs.setInt(4, currentStock);
                        insertPs.setInt(5, threshold);
                        insertPs.executeUpdate();
                    }
                }
            }
        }
    }
    
    // FIX: Added wrapper method that creates its own connection
    private void checkAndCreateStockAlert(int productId, Integer variantId, 
                                         int currentStock, int threshold) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            checkAndCreateStockAlert(conn, productId, variantId, currentStock, threshold);
        }
    }
    
    // ===== Helper Methods =====
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setDetailedDescription(rs.getString("detailed_description"));
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setHasVariants(rs.getBoolean("has_variants"));
        product.setSpecifications(rs.getString("specifications"));
        
        Array tagsArray = rs.getArray("tags");
        if (tagsArray != null) {
            product.setTags((String[]) tagsArray.getArray());
        }
        
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return product;
    }
    
    private ProductVariant mapResultSetToVariant(ResultSet rs) throws SQLException {
        ProductVariant variant = new ProductVariant();
        variant.setId(rs.getInt("id"));
        variant.setProductId(rs.getInt("product_id"));
        variant.setSku(rs.getString("sku"));
        variant.setSize(rs.getString("size"));
        variant.setColor(rs.getString("color"));
        variant.setMaterial(rs.getString("material"));
        variant.setPrice(rs.getDouble("price"));
        variant.setStockQuantity(rs.getInt("stock_quantity"));
        variant.setLowStockThreshold(rs.getInt("low_stock_threshold"));
        variant.setImageUrl(rs.getString("image_url"));
        variant.setActive(rs.getBoolean("is_active"));
        variant.setCreatedAt(rs.getTimestamp("created_at"));
        variant.setUpdatedAt(rs.getTimestamp("updated_at"));
        return variant;
    }
    
    private StockAlert mapResultSetToStockAlert(ResultSet rs) throws SQLException {
        StockAlert alert = new StockAlert();
        alert.setId(rs.getInt("id"));
        alert.setProductId(rs.getInt("product_id"));
        
        int variantId = rs.getInt("variant_id");
        if (!rs.wasNull()) {
            alert.setVariantId(variantId);
        }
        
        alert.setProductName(rs.getString("product_name"));
        alert.setVariantInfo(rs.getString("variant_info"));
        alert.setAlertType(rs.getString("alert_type"));
        alert.setCurrentStock(rs.getInt("current_stock"));
        alert.setThresholdValue(rs.getInt("threshold_value"));
        alert.setStatus(rs.getString("status"));
        alert.setCreatedAt(rs.getTimestamp("created_at"));
        
        return alert;
    }
}
