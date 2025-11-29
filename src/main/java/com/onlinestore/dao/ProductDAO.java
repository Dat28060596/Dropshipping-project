package com.onlinestore.dao;

import com.onlinestore.model.Product;
import com.onlinestore.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "ORDER BY p.id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (name, description, price, stock, supplier_id, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            
             if (p.getSupplierId() > 0) {
                stmt.setInt(5, p.getSupplierId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            if (p.getCategoryId() > 0) {
                stmt.setInt(6, p.getCategoryId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setSupplierId(rs.getInt("supplier_id"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock=?, supplier_id=?, category_id=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            
            if (p.getSupplierId() > 0) {
                stmt.setInt(5, p.getSupplierId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            if (p.getCategoryId() > 0) {
                stmt.setInt(6, p.getCategoryId());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            stmt.setInt(7, p.getId()); 
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getProductsBySupplier(int supplierId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.supplier_id = ? " +  
                     "ORDER BY p.id ASC";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}