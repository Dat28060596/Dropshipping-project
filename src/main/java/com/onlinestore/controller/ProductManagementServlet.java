package com.onlinestore.controller;

import com.onlinestore.model.User;
import com.onlinestore.dao.ProductDAO;
import com.onlinestore.model.Product;
import com.onlinestore.model.ProductVariant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/products/*")
public class ProductManagementServlet extends HttpServlet {
    
    // FIX 1: Changed variable name from ProductDAO to productDAO (lowercase)
    private ProductDAO productDAO = new ProductDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all products
                listProducts(request, response);
            } else if (pathInfo.equals("/add")) {
                // Show add product form
                request.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp")
                       .forward(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                // Show edit product form
                int productId = Integer.parseInt(pathInfo.substring(6));
                editProduct(request, response, productId);
            } else if (pathInfo.startsWith("/variants/")) {
                // Manage product variants
                int productId = Integer.parseInt(pathInfo.substring(10));
                manageVariants(request, response, productId);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createProduct(request, response);
            } else if ("update".equals(action)) {
                updateProduct(request, response);
            } else if ("delete".equals(action)) {
                deleteProduct(request, response);
            } else if ("addVariant".equals(action)) {
                addVariant(request, response);
            } else if ("updateVariant".equals(action)) {
                updateVariantMethod(request, response);
            } else if ("updateStock".equals(action)) {
                updateStock(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        // FIX 2: Changed ProductDAO to productDAO
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/product-list.jsp")
               .forward(request, response);
    }
    
    private void editProduct(HttpServletRequest request, HttpServletResponse response, int productId)
            throws SQLException, ServletException, IOException {
        
        // FIX 3: Changed ProductDAO to productDAO
        Product product = productDAO.getProductById(productId);
        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp")
               .forward(request, response);
    }
    
    private void manageVariants(HttpServletRequest request, HttpServletResponse response, int productId)
            throws SQLException, ServletException, IOException {
        
        // FIX 4: Changed ProductDAO to productDAO
        Product product = productDAO.getProductById(productId);
        List<ProductVariant> variants = productDAO.getProductVariants(productId);
        
        request.setAttribute("product", product);
        request.setAttribute("variants", variants);
        request.getRequestDispatcher("/WEB-INF/views/admin/product-variants.jsp")
               .forward(request, response);
    }
    
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setDetailedDescription(request.getParameter("detailedDescription"));
        
        // FIX 5: Changed from new double() to Double.parseDouble()
        product.setPrice(Double.parseDouble(request.getParameter("price")));
        
        product.setStock(Integer.parseInt(request.getParameter("stock")));
        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        product.setHasVariants("true".equals(request.getParameter("hasVariants")));
        product.setSpecifications(request.getParameter("specifications"));
        
        String tags = request.getParameter("tags");
        if (tags != null && !tags.trim().isEmpty()) {
            product.setTags(tags.split(","));
        }
        
        // FIX 6: Changed ProductDAO to productDAO
        int productId = productDAO.createProduct(product);
        
        response.sendRedirect(request.getContextPath() + "/admin/products?msg=created");
    }
    
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        Product product = new Product();
        product.setId(Integer.parseInt(request.getParameter("id")));
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setDetailedDescription(request.getParameter("detailedDescription"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));
        product.setStock(Integer.parseInt(request.getParameter("stock")));
        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        product.setHasVariants("true".equals(request.getParameter("hasVariants")));
        product.setSpecifications(request.getParameter("specifications"));
        
        String tags = request.getParameter("tags");
        if (tags != null && !tags.trim().isEmpty()) {
            product.setTags(tags.split(","));
        }
        
        // FIX 7: Changed ProductDAO to productDAO
        productDAO.updateProduct(product);
        
        response.sendRedirect(request.getContextPath() + "/admin/products?msg=updated");
    }
    
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int productId = Integer.parseInt(request.getParameter("id"));
        
        // FIX 8: Changed ProductDAO to productDAO
        productDAO.deleteProduct(productId);
        
        response.sendRedirect(request.getContextPath() + "/admin/products?msg=deleted");
    }
    
    private void addVariant(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        ProductVariant variant = new ProductVariant();
        variant.setProductId(Integer.parseInt(request.getParameter("productId")));
        variant.setSku(request.getParameter("sku"));
        variant.setSize(request.getParameter("size"));
        variant.setColor(request.getParameter("color"));
        variant.setMaterial(request.getParameter("material"));
        
        // FIX 9: Changed from BigDecimal to double
        variant.setPrice(Double.parseDouble(request.getParameter("price")));
        
        variant.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        variant.setLowStockThreshold(Integer.parseInt(request.getParameter("lowStockThreshold")));
        variant.setImageUrl(request.getParameter("imageUrl"));
        variant.setActive(true);
        
        // FIX 10: Changed ProductDAO to productDAO
        productDAO.createVariant(variant);
        
        response.sendRedirect(request.getContextPath() + "/admin/products/variants/" + 
                             variant.getProductId() + "?msg=variantAdded");
    }
    
    private void updateVariantMethod(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        ProductVariant variant = new ProductVariant();
        variant.setId(Integer.parseInt(request.getParameter("id")));
        variant.setProductId(Integer.parseInt(request.getParameter("productId")));
        variant.setSku(request.getParameter("sku"));
        variant.setSize(request.getParameter("size"));
        variant.setColor(request.getParameter("color"));
        variant.setMaterial(request.getParameter("material"));
        
        // FIX 11: Changed from BigDecimal to double
        variant.setPrice(Double.parseDouble(request.getParameter("price")));
        
        variant.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        variant.setLowStockThreshold(Integer.parseInt(request.getParameter("lowStockThreshold")));
        variant.setImageUrl(request.getParameter("imageUrl"));
        variant.setActive("true".equals(request.getParameter("isActive")));
        
        // FIX 12: Changed ProductDAO to productDAO
        productDAO.updateVariant(variant);
        
        response.sendRedirect(request.getContextPath() + "/admin/products/variants/" + 
                             variant.getProductId() + "?msg=variantUpdated");
    }
    
    private void updateStock(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int productId = Integer.parseInt(request.getParameter("productId"));
        String variantIdStr = request.getParameter("variantId");
        Integer variantId = (variantIdStr != null && !variantIdStr.isEmpty()) ? 
                           Integer.parseInt(variantIdStr) : null;
        
        int quantityChange = Integer.parseInt(request.getParameter("quantityChange"));
        String changeType = request.getParameter("changeType"); // STOCK_IN, STOCK_OUT, ADJUSTMENT
        String reason = request.getParameter("reason");
        
        // FIX 13: Simplified the User cast
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        int userId = currentUser.getId();
        
        // FIX 14: Changed ProductDAO to productDAO
        productDAO.updateStock(productId, variantId, quantityChange, changeType, reason, userId);
        
        if (variantId != null) {
            response.sendRedirect(request.getContextPath() + "/admin/products/variants/" + 
                                 productId + "?msg=stockUpdated");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products?msg=stockUpdated");
        }
    }
}
