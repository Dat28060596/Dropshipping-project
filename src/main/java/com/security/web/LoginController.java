package com.security.web;

import java.io.IOException;

import com.security.dao.UserDao;
import com.security.model.User;
import com.security.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/Login"})
public class LoginController extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect to login.jsp when GET request comes
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Look up the user from DB
        User user = userDao.findByUsername(username);

        if (user == null) {
            // Wrong username
            request.setAttribute("error", "Invalid username or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Compare passwords
        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            request.setAttribute("error", "Invalid username or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Check enabled
        if (!user.getEnabled()) {
            request.setAttribute("error", "Your account is disabled!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Login success → Save user in session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // Redirect to dashboard/home
        response.sendRedirect("dashboard.jsp");
    }

}
