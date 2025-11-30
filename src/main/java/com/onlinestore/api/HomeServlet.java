package com.onlinestore.api;

import com.onlinestore.dao.ProductDAO;
import com.onlinestore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home_servlet"}) 
public class HomeServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // FIX: Wrap in try-catch to handle SQLException
            List<Product> products = productDAO.getAllProducts();
            
            request.setAttribute("productList", products);
            
            request.getRequestDispatcher("./home.jsp").forward(request, response);
            
        } catch (SQLException e) {
            // Handle database error
            throw new ServletException("Database error while fetching products", e);
        }
    }
}
