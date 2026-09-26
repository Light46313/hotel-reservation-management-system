package hotel.database;

import hotel.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // =========================
    // ADD CUSTOMER
    // =========================

    public void addCustomer(Customer customer) {

        String sql = """
                INSERT INTO customers
                (
                    customer_id,
                    name,
                    phone,
                    email,
                    address,
                    photo_path
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    customer.getCustomerId()
            );

            statement.setString(
                    2,
                    customer.getName()
            );

            statement.setString(
                    3,
                    customer.getPhone()
            );

            statement.setString(
                    4,
                    customer.getEmail()
            );

            statement.setString(
                    5,
                    customer.getAddress()
            );

            statement.setString(
                    6,
                    customer.getPhotoPath()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not add customer to database.",
                    e
            );
        }
    }


    // =========================
    // GET ALL CUSTOMERS
    // =========================

    public List<Customer> getAllCustomers() {

        List<Customer> customers =
                new ArrayList<>();

        String sql = """
                SELECT
                    customer_id,
                    name,
                    phone,
                    email,
                    address,
                    photo_path
                FROM customers
                ORDER BY customer_id
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Customer customer =
                        createCustomerFromResultSet(
                                resultSet
                        );

                customers.add(customer);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return customers;
    }


    // =========================
    // GET CUSTOMER BY ID
    // =========================

    public Customer getCustomerById(
            int customerId
    ) {

        String sql = """
                SELECT
                    customer_id,
                    name,
                    phone,
                    email,
                    address,
                    photo_path
                FROM customers
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    customerId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return createCustomerFromResultSet(
                            resultSet
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================
    // UPDATE CUSTOMER
    // =========================

    public void updateCustomer(
            Customer customer
    ) {

        String sql = """
                UPDATE customers
                SET
                    name = ?,
                    phone = ?,
                    email = ?,
                    address = ?,
                    photo_path = ?
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    customer.getName()
            );

            statement.setString(
                    2,
                    customer.getPhone()
            );

            statement.setString(
                    3,
                    customer.getEmail()
            );

            statement.setString(
                    4,
                    customer.getAddress()
            );

            statement.setString(
                    5,
                    customer.getPhotoPath()
            );

            statement.setInt(
                    6,
                    customer.getCustomerId()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not update customer.",
                    e
            );
        }
    }


    // =========================
    // DELETE CUSTOMER
    // =========================

    public void deleteCustomer(
            int customerId
    ) {

        String sql = """
                DELETE FROM customers
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    customerId
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not delete customer.",
                    e
            );
        }
    }


    // =========================
    // SEARCH CUSTOMERS
    // =========================

    public List<Customer> searchCustomers(
            String searchText
    ) {

        List<Customer> customers =
                new ArrayList<>();

        String sql = """
                SELECT
                    customer_id,
                    name,
                    phone,
                    email,
                    address,
                    photo_path
                FROM customers
                WHERE
                    CAST(customer_id AS TEXT) LIKE ?
                    OR name LIKE ?
                    OR phone LIKE ?
                    OR email LIKE ?
                    OR address LIKE ?
                ORDER BY customer_id
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String searchPattern =
                    "%" + searchText + "%";

            statement.setString(
                    1,
                    searchPattern
            );

            statement.setString(
                    2,
                    searchPattern
            );

            statement.setString(
                    3,
                    searchPattern
            );

            statement.setString(
                    4,
                    searchPattern
            );

            statement.setString(
                    5,
                    searchPattern
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Customer customer =
                            createCustomerFromResultSet(
                                    resultSet
                            );

                    customers.add(customer);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return customers;
    }


    // =========================
    // CREATE CUSTOMER FROM
    // RESULT SET
    // =========================

    private Customer createCustomerFromResultSet(
            ResultSet resultSet
    ) throws Exception {

        Customer customer =
                new Customer(
                        resultSet.getInt(
                                "customer_id"
                        ),

                        resultSet.getString(
                                "name"
                        ),

                        resultSet.getString(
                                "phone"
                        ),

                        resultSet.getString(
                                "email"
                        ),

                        resultSet.getString(
                                "address"
                        )
                );

        customer.setPhotoPath(
                resultSet.getString(
                        "photo_path"
                )
        );

        return customer;
    }
}