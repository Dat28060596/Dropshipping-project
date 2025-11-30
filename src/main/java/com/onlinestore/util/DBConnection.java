package com.onlinestore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Supabase Connection using Supavisor Pooler
    private static final String URL = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?prepareThreshold=0";
    
    // IMPORTANT: Username must be in format postgres.YOUR_PROJECT_REF
    private static final String USER = "postgres.kdvnogkvnpvbnedsmoui";  // Changed from just "postgres"
    private static final String PASS = "thisisnotai123";

    static {
        try { 
            Class.forName("org.postgresql.Driver"); 
        } catch (ClassNotFoundException e) { 
            e.printStackTrace(); 
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
