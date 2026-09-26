package hotel.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseVerification {

    public static void main(String[] args) {

        String sql = """
                SELECT name
                FROM sqlite_master
                WHERE type = 'table'
                AND name NOT LIKE 'sqlite_%'
                ORDER BY name
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                Statement statement =
                        connection.createStatement();

                ResultSet resultSet =
                        statement.executeQuery(sql)
        ) {

            System.out.println("SQLite Tables:");

            while (resultSet.next()) {

                String tableName =
                        resultSet.getString("name");

                System.out.println(
                        "- " + tableName
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Could not verify database tables."
            );

            e.printStackTrace();
        }
    }
}