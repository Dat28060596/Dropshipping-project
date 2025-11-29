package com.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Logout Controller
 * Handles user logout, clears session, and redirects to landing page
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout", "/api/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get current session
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Invalidate session - clears all session attributes
            session.invalidate();
        }

        // Redirect to landing page
        response.sendRedirect(request.getContextPath() + "/landing");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Treat POST same as GET for logout
        doGet(request, response);
    }
}
