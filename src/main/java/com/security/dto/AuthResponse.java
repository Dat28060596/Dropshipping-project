package com.security.dto;

import java.util.List;

/**
 * DTO for successful authentication responses sent to React frontend
 * Contains JWT token and user information
 */
public class AuthResponse {
    private boolean success;
    private String token;
    private String username;
    private List<String> roles;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(boolean success, String token, String username, List<String> roles) {
        this.success = success;
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.message = "Login successful";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
