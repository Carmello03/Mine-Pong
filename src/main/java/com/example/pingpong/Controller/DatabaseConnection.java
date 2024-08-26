package com.example.pingpong.Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the creation and management of database connections.
 * This class provides a global access point to the database connection
 * using the Singleton design pattern to ensure that only one connection
 * is active at any given time.
 */
public class DatabaseConnection {
    private static Connection connection = null;

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private DatabaseConnection() { }

    /**
     * Gets a single instance of a database connection.
     * If the current connection is null or closed, it initializes a new connection.
     *
     * @return A single {@link Connection} instance to be used across the application.
     * @throws SQLException If there is an error connecting to the database.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://localhost:3306/ping-pong-database";
            String username = "root";
            String password = "root";
            try {
                // Initialize JDBC driver, setup URL, username, and password
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}

