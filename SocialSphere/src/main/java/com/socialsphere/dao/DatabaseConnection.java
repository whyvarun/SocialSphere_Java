package com.socialsphere.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:h2:C:\\Users\\VD\\test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static Connection connection;
    private static boolean initialized = false; // Flag to track initialization status

    // Static block to initialize the database connection and schema
    static {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeDatabase();
            initialized = true; // Set flag to true upon successful initialization
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            // Do not set initialized to true if driver not found
        } catch (SQLException e) {
            System.err.println("Database connection or initialization failed: " + e.getMessage());
            e.printStackTrace();
            // Do not set initialized to true if SQL error occurs
        }
    }

    /**
     * Returns the database connection. Throws SQLException if connection is not established.
     * @return The active database connection.
     * @throws SQLException If the database connection is null or not initialized.
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized || connection == null) {
            throw new SQLException("Database connection is not initialized or failed to connect.");
        }
        return connection;
    }

    private static void initializeDatabase() throws SQLException {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            // Create Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create Posts table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS posts (
                    post_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    title VARCHAR(200) NOT NULL,
                    content TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                )
            """);

            // Create Comments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS comments (
                    comment_id INT AUTO_INCREMENT PRIMARY KEY,
                    post_id INT NOT NULL,
                    user_id INT NOT NULL,
                    content TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (post_id) REFERENCES posts(post_id),
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                )
            """);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
