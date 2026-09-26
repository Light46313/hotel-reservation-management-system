package hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // SQLite database file
    private static final String URL = "jdbc:sqlite:hotel.db";

    // Private constructor
    private DatabaseConnection() {
    }

    // Create and return a database connection
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL);
    }
}