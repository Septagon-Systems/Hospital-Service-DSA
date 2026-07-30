package edu.ug.dcit308.hospital.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton JDBC Connection Factory for SQLite DB.
 */
public class ConnectionManager {
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:data/hospital_system.db";
    private static Connection connection;

    private ConnectionManager() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DEFAULT_DB_URL);
        }
        return connection;
    }

    public static synchronized Connection getInMemoryConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }
}
