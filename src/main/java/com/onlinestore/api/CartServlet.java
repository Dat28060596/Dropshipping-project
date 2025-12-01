package com.onlinestore.api;

import com.onlinestore.dao.ProductDAO;
import com.onlinestore.model.CartItem;
import com.onlinestore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = "/cart")
public class CartServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // FIX: Wrap database operations in try-catch
        try {
            if ("add".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                boolean found = false;
                for (CartItem item : cart) {
                    if (item.getProduct().getId() == productId) {
                        item.setQuantity(item.getQuantity() + quantity);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    // FIX: Handle SQLException from getProductById
                    Product p = productDAO.getProductById(productId);
                    if (p != null) {
                        cart.add(new CartItem(p, quantity));
                    }
                }

                response.sendRedirect(request.getContextPath() + "/home_servlet?msg=added");
                return; 
            } 
            else if ("remove".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                cart.removeIf(item -> item.getProduct().getId() == productId);
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (SQLException e) {
            // Handle database error
            throw new ServletException("Database error while processing cart", e);
        }
    }
}
