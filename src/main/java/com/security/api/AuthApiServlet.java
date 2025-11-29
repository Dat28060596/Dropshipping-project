package com.security.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.security.dao.UserDao;
import com.security.dto.AuthResponse;
import com.security.dto.LoginRequest;
import com.security.dto.RegisterRequest;
import com.security.model.User;
import com.security.util.JwtUtil;
import com.security.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * REST API Servlet for Authentication
 * Handles POST /api/auth/login and POST /api/auth/register
 * Returns JSON responses for React frontend
 */
@WebServlet(name = "AuthApiServlet", urlPatterns = {"/api/auth/*"})
public class AuthApiServlet extends HttpServlet {

    private UserDao userDao = new UserDao();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            sendError(response, 400, "Invalid endpoint");
            return;
        }

        if (pathInfo.equals("/login")) {
            handleLogin(request, response);
        } else if (pathInfo.equals("/register")) {
            handleRegister(request, response);
        } else {
            sendError(response, 404, "Endpoint not found");
        }
    }

    /**
     * Handle login endpoint: POST /api/auth/login
     * Expected JSON body: {"username": "user", "password": "pass"}
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Parse JSON request body
            LoginRequest loginRequest = parseJsonBody(request, LoginRequest.class);

            if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                sendError(response, 400, "Username and password are required");
                return;
            }

            // Validate credentials
            User user = userDao.findByUsername(loginRequest.getUsername());

            if (user == null) {
                sendError(response, 401, "Invalid username or password");
                return;
            }

            // Check if user is enabled
            if (!user.getEnabled()) {
                sendError(response, 403, "User account is disabled");
                return;
            }

            // Verify password using BCrypt
            if (!PasswordUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                sendError(response, 401, "Invalid username or password");
                return;
            }

            // Generate JWT token
            String token = JwtUtil.generateToken(user.getUsername(), user.getRoles());

            // Prepare successful response
            AuthResponse authResponse = new AuthResponse(
                    true,
                    token,
                    user.getUsername(),
                    new ArrayList<>(user.getRoles())
            );

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(authResponse));

        } catch (Exception e) {
            sendError(response, 500, "Internal server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle register endpoint: POST /api/auth/register
     * Expected JSON body: {"username": "user", "password": "pass", "email": "email@example.com", "fullName": "Full Name"}
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Parse JSON request body
            RegisterRequest registerRequest = parseJsonBody(request, RegisterRequest.class);

            if (registerRequest == null) {
                sendError(response, 400, "Invalid request body");
                return;
            }

            // Validate required fields
            if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
                sendError(response, 400, "Username is required");
                return;
            }
            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                sendError(response, 400, "Password is required");
                return;
            }
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                sendError(response, 400, "Email is required");
                return;
            }

            // Validate email format (basic validation)
            if (!registerRequest.getEmail().contains("@")) {
                sendError(response, 400, "Invalid email format");
                return;
            }

            // Validate password strength (minimum 6 characters)
            if (registerRequest.getPassword().length() < 6) {
                sendError(response, 400, "Password must be at least 6 characters long");
                return;
            }

            // Create new user
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(registerRequest.getPassword()); // Will be hashed in DAO
            newUser.setEmail(registerRequest.getEmail());
            newUser.setFullName(registerRequest.getFullName() != null ? registerRequest.getFullName() : "");
            newUser.setEnabled(true);

            // Save user to database
            if (!userDao.save(newUser)) {
                sendError(response, 400, "Registration failed: Username or email already exists");
                return;
            }

            // Generate JWT token for new user
            String token = JwtUtil.generateToken(newUser.getUsername(), newUser.getRoles());

            // Prepare successful response
            AuthResponse authResponse = new AuthResponse(
                    true,
                    token,
                    newUser.getUsername(),
                    new ArrayList<>(newUser.getRoles())
            );
            authResponse.setMessage("Registration successful");

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(authResponse));

        } catch (Exception e) {
            sendError(response, 500, "Internal server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parse JSON body from request
     */
    private <T> T parseJsonBody(HttpServletRequest request, Class<T> clazz) {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return gson.fromJson(sb.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Send error response as JSON
     */
    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        AuthResponse errorResponse = new AuthResponse(false, message);
        response.getWriter().write(gson.toJson(errorResponse));
    }
}
