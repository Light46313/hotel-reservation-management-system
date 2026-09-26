package hotel.database;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println(
                    "SQLite database connected successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Database connection failed."
            );

            e.printStackTrace();
        }
    }
}