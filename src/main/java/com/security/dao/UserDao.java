package com.security.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import com.security.model.User;
import com.security.util.PasswordUtil;

public class UserDao {

    private final String jdbcURL = "jdbc:postgresql://db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres";
    private final String jdbcUser = "postgres";
    private final String jdbcPass = "thisisnotai123";

    private Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
    }

    /**
     * Find user by username
     * @param username The username to search for
     * @return User object with roles, or null if not found
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                u.setFullName(rs.getString("full_name"));
                u.setEnabled(rs.getBoolean("enabled"));

                // Fetch roles
                u.setRoles(findRolesByUserId(u.getId()));

                return u;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }
    

    /**
     * Find user by ID
     * @param userId The user ID
     * @return User object with roles, or null if not found
     */
    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                u.setFullName(rs.getString("full_name"));
                u.setEnabled(rs.getBoolean("enabled"));

                // Fetch roles
                u.setRoles(findRolesByUserId(u.getId()));

                return u;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }

    /**
     * Check if username already exists
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Check if email already exists
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE email=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Save new user to database (for registration)
     * @param user The user to save (password must be plain text, will be hashed)
     * @return true if successful, false otherwise
     */
    public boolean save(User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.out.println("Username cannot be empty");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.out.println("Password cannot be empty");
            return false;
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Email cannot be empty");
            return false;
        }

        // Check if username already exists
        if (usernameExists(user.getUsername())) {
            System.out.println("Username already exists");
            return false;
        }

        // Check if email already exists
        if (emailExists(user.getEmail())) {
            System.out.println("Email already exists");
            return false;
        }

        String sql = "INSERT INTO users (username, password, email, full_name, enabled) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hash password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName() != null ? user.getFullName() : "");
            ps.setBoolean(5, true); // Enable user by default

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Get generated ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        
                        // Assign default role (USER)
                        assignRoleToUser(user.getId(), "USER");
                        
                        return true;
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Update existing user
     * @param user The user with updated information
     * @return true if successful
     */
    public boolean updateUser(User user) {
        StringBuilder sql = new StringBuilder("UPDATE users SET email=?, full_name=?");
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            sql.append(", password=?");
        }
        sql.append(" WHERE username=?");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            
            int paramIndex = 3;
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Hash new password
                String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
                ps.setString(paramIndex++, hashedPassword);
            }
            
            ps.setString(paramIndex, user.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Assign role to user
     * @param userId The user ID
     * @param role The role to assign
     * @return true if successful, false otherwise
     */
    public boolean assignRoleToUser(Long userId, String role) {
        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, role);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Fetch user roles from database
     * @param userId The user ID
     * @return HashSet of role names
     */
    private HashSet<String> findRolesByUserId(Long userId) {
        HashSet<String> roles = new HashSet<>();
        String sql = "SELECT role FROM user_roles WHERE user_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }

        return roles;
    }
}