package com.onlinestore.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // YOUR Supabase Credentials
    private static final String URL = "jdbc:postgresql://db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "thisisnotai123";

    static {
        try { Class.forName("org.postgresql.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}