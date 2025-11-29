package com.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Landing Page Controller
 * Displays public landing page for unauthenticated users
 */
@WebServlet(name = "LandingPageServlet", urlPatterns = {"/", "/landing", "/home"})
public class LandingPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set attributes for JSP
        request.setAttribute("title", "Dropshipping Store Management");
        request.setAttribute("currentPage", "landing");
        
        // Forward to landing page
        request.getRequestDispatcher("landing.jsp").forward(request, response);
    }
}
