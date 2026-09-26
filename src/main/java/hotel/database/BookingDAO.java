package hotel.database;

import hotel.model.Booking;
import hotel.model.Customer;
import hotel.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // =========================================================
    // INSERT BOOKING
    // =========================================================

    public void insertBooking(Booking booking) {

        String sql = """
                INSERT INTO bookings
                (
                    booking_id,
                    customer_id,
                    room_number,
                    check_in,
                    check_out
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    booking.getBookingId()
            );

            statement.setInt(
                    2,
                    booking.getCustomer()
                            .getCustomerId()
            );

            statement.setInt(
                    3,
                    booking.getRoom()
                            .getRoomNumber()
            );

            statement.setString(
                    4,
                    booking.getCheckIn()
                            .toString()
            );

            statement.setString(
                    5,
                    booking.getCheckOut()
                            .toString()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not insert booking into database.",
                    e
            );
        }
    }


    // =========================================================
    // ADD BOOKING
    // =========================================================
    // Kept for compatibility with older controller code.

    public void addBooking(Booking booking) {

        insertBooking(booking);
    }


    // =========================================================
    // GET ALL BOOKINGS
    // =========================================================

    public List<Booking> getAllBookings() {

        List<Booking> bookings =
                new ArrayList<>();

        String sql = """
                SELECT
                    b.booking_id,
                    b.check_in,
                    b.check_out,

                    c.customer_id,
                    c.name,
                    c.phone,
                    c.email,
                    c.address,
                    c.photo_path,

                    r.room_number,
                    r.room_type,
                    r.price,
                    r.available

                FROM bookings b

                JOIN customers c
                    ON b.customer_id = c.customer_id

                JOIN rooms r
                    ON b.room_number = r.room_number

                ORDER BY b.booking_id
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

                // =================================================
                // CREATE CUSTOMER OBJECT
                // =================================================

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


                // =================================================
                // CREATE ROOM OBJECT
                // =================================================

                Room room =
                        new Room(
                                resultSet.getInt(
                                        "room_number"
                                ),

                                resultSet.getString(
                                        "room_type"
                                ),

                                resultSet.getDouble(
                                        "price"
                                ),

                                resultSet.getBoolean(
                                        "available"
                                )
                        );


                // =================================================
                // CREATE BOOKING OBJECT
                // =================================================

                Booking booking =
                        new Booking(
                                resultSet.getInt(
                                        "booking_id"
                                ),

                                customer,

                                room,

                                LocalDate.parse(
                                        resultSet.getString(
                                                "check_in"
                                        )
                                ),

                                LocalDate.parse(
                                        resultSet.getString(
                                                "check_out"
                                        )
                                )
                        );


                bookings.add(booking);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return bookings;
    }


    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    public Booking getBookingById(
            int bookingId
    ) {

        String sql = """
                SELECT
                    b.booking_id,
                    b.check_in,
                    b.check_out,

                    c.customer_id,
                    c.name,
                    c.phone,
                    c.email,
                    c.address,
                    c.photo_path,

                    r.room_number,
                    r.room_type,
                    r.price,
                    r.available

                FROM bookings b

                JOIN customers c
                    ON b.customer_id = c.customer_id

                JOIN rooms r
                    ON b.room_number = r.room_number

                WHERE b.booking_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    // =============================================
                    // CUSTOMER
                    // =============================================

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


                    // =============================================
                    // ROOM
                    // =============================================

                    Room room =
                            new Room(
                                    resultSet.getInt(
                                            "room_number"
                                    ),

                                    resultSet.getString(
                                            "room_type"
                                    ),

                                    resultSet.getDouble(
                                            "price"
                                    ),

                                    resultSet.getBoolean(
                                            "available"
                                    )
                            );


                    // =============================================
                    // BOOKING
                    // =============================================

                    return new Booking(
                            resultSet.getInt(
                                    "booking_id"
                            ),

                            customer,

                            room,

                            LocalDate.parse(
                                    resultSet.getString(
                                            "check_in"
                                    )
                            ),

                            LocalDate.parse(
                                    resultSet.getString(
                                            "check_out"
                                    )
                            )
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // UPDATE BOOKING
    // =========================================================

    public void updateBooking(
            Booking booking
    ) {

        String sql = """
                UPDATE bookings
                SET
                    customer_id = ?,
                    room_number = ?,
                    check_in = ?,
                    check_out = ?
                WHERE booking_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    booking.getCustomer()
                            .getCustomerId()
            );

            statement.setInt(
                    2,
                    booking.getRoom()
                            .getRoomNumber()
            );

            statement.setString(
                    3,
                    booking.getCheckIn()
                            .toString()
            );

            statement.setString(
                    4,
                    booking.getCheckOut()
                            .toString()
            );

            statement.setInt(
                    5,
                    booking.getBookingId()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not update booking.",
                    e
            );
        }
    }


    // =========================================================
    // DELETE BOOKING
    // =========================================================

    public void deleteBooking(
            int bookingId
    ) {

        String sql = """
                DELETE FROM bookings
                WHERE booking_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not delete booking.",
                    e
            );
        }
    }


    // =========================================================
    // GET BOOKINGS BY CUSTOMER
    // =========================================================

    public List<Booking> getBookingsByCustomer(
            int customerId
    ) {

        List<Booking> bookings =
                new ArrayList<>();

        String sql = """
                SELECT
                    b.booking_id,
                    b.check_in,
                    b.check_out,

                    c.customer_id,
                    c.name,
                    c.phone,
                    c.email,
                    c.address,
                    c.photo_path,

                    r.room_number,
                    r.room_type,
                    r.price,
                    r.available

                FROM bookings b

                JOIN customers c
                    ON b.customer_id = c.customer_id

                JOIN rooms r
                    ON b.room_number = r.room_number

                WHERE b.customer_id = ?

                ORDER BY b.booking_id
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

                while (resultSet.next()) {

                    // =============================================
                    // CUSTOMER
                    // =============================================

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


                    // =============================================
                    // ROOM
                    // =============================================

                    Room room =
                            new Room(
                                    resultSet.getInt(
                                            "room_number"
                                    ),

                                    resultSet.getString(
                                            "room_type"
                                    ),

                                    resultSet.getDouble(
                                            "price"
                                    ),

                                    resultSet.getBoolean(
                                            "available"
                                    )
                            );


                    // =============================================
                    // BOOKING
                    // =============================================

                    Booking booking =
                            new Booking(
                                    resultSet.getInt(
                                            "booking_id"
                                    ),

                                    customer,

                                    room,

                                    LocalDate.parse(
                                            resultSet.getString(
                                                    "check_in"
                                            )
                                    ),

                                    LocalDate.parse(
                                            resultSet.getString(
                                                    "check_out"
                                            )
                                    )
                            );


                    bookings.add(booking);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return bookings;
    }
}