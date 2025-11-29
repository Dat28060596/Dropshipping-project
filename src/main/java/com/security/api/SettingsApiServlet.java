package com.security.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.security.dao.UserDao;
import com.security.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SettingsApiServlet", urlPatterns = {"/api/settings/update"})
public class SettingsApiServlet extends HttpServlet {

    private UserDao userDao = new UserDao();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get current logged-in user from session (legacy) or JWT (if fully migrated)
        // For this hybrid setup, we'll check the session first as settings.jsp uses session
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            sendError(response, 401, "Unauthorized. Please login.");
            return;
        }

        try {
            // Parse request body
            UserUpdateDTO updateData = parseJsonBody(request, UserUpdateDTO.class);

            if (updateData == null) {
                sendError(response, 400, "Invalid request data");
                return;
            }

            // Prepare user object for update
            User userToUpdate = new User();
            userToUpdate.setUsername(sessionUser.getUsername()); // Username is immutable key
            userToUpdate.setFullName(updateData.fullName);
            userToUpdate.setEmail(updateData.email);
            
            if (updateData.newPassword != null && !updateData.newPassword.isEmpty()) {
                if (updateData.newPassword.length() < 6) {
                    sendError(response, 400, "Password must be at least 6 characters");
                    return;
                }
                userToUpdate.setPassword(updateData.newPassword);
            }

            // Perform update
            if (userDao.updateUser(userToUpdate)) {
                // Update session object to reflect changes immediately
                sessionUser.setFullName(updateData.fullName);
                sessionUser.setEmail(updateData.email);
                // Do not store password in session object
                
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("success", true);
                successResponse.put("message", "Profile updated successfully!");
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(successResponse));
            } else {
                sendError(response, 500, "Failed to update profile in database");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, 500, "Internal Server Error: " + e.getMessage());
        }
    }

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

    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        response.getWriter().write(gson.toJson(error));
    }

    // DTO class for JSON mapping
    private static class UserUpdateDTO {
        String fullName;
        String email;
        String newPassword;
    }
}