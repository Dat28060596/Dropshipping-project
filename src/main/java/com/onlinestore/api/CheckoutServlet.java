package com.onlinestore.api;

import com.onlinestore.dao.OrderDAO;
import com.onlinestore.model.CartItem;
import com.onlinestore.util.dichvuvanchuyen; // Friend 2's Code
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/order/checkout-cart") // Matches action in Friend 1's cart.jsp
public class CheckoutServlet extends HttpServlet {
    
    private OrderDAO orderDAO = new OrderDAO();
    private dichvuvanchuyen shippingService = new dichvuvanchuyen();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        
        // 1. Get User (Your Auth)
        // Ensure your LoginController saves "userId" as Long in session!
        Long userId = (Long) session.getAttribute("userId"); 
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. Get Cart
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }

        // 3. Logic from Friend 2 (Shipping)
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        double shippingFee = shippingService.tinhPhiShip(address); // Copied method name

        // 4. Calculate Total
        double productTotal = 0;
        for (CartItem item : cart) productTotal += item.getProduct().getPrice() * item.getQuantity();
        double grandTotal = productTotal + shippingFee;

        // 5. Logic from Friend 1 (Create Order)
        long orderId = orderDAO.createOrderFromCart(userId, grandTotal, cart, address, phone);

        // 6. Redirect to Your Payment (VNPay)
        if (orderId > 0) {
            // Clear cart
            session.removeAttribute("cart");
            // Go to your VNPay Servlet
            response.sendRedirect(request.getContextPath() + "/payment-process?orderId=" + orderId + "&amount=" + (long)grandTotal + "&orderInfo=PayOrder");
        } else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}