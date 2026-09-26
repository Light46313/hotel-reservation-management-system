package hotel.database;

import hotel.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // =========================
    // ADD ROOM
    // =========================

    public void addRoom(Room room) {

        String sql = """
                INSERT INTO rooms
                (
                    room_number,
                    room_type,
                    price,
                    available
                )
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    room.getRoomNumber()
            );

            statement.setString(
                    2,
                    room.getRoomType()
            );

            statement.setDouble(
                    3,
                    room.getPrice()
            );

            statement.setInt(
                    4,
                    room.isAvailable() ? 1 : 0
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not add room to database.",
                    e
            );
        }
    }


    // =========================
    // GET ALL ROOMS
    // =========================

    public List<Room> getAllRooms() {

        List<Room> rooms =
                new ArrayList<>();

        String sql = """
                SELECT
                    room_number,
                    room_type,
                    price,
                    available
                FROM rooms
                ORDER BY room_number
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

                int roomNumber =
                        resultSet.getInt("room_number");

                String roomType =
                        resultSet.getString("room_type");

                double price =
                        resultSet.getDouble("price");

                boolean available =
                        resultSet.getInt("available") == 1;

                Room room =
                        new Room(
                                roomNumber,
                                roomType,
                                price,
                                available
                        );

                rooms.add(room);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return rooms;
    }


    // =========================
    // GET ROOM BY NUMBER
    // =========================

    public Room getRoomByNumber(int roomNumber) {

        String sql = """
                SELECT
                    room_number,
                    room_type,
                    price,
                    available
                FROM rooms
                WHERE room_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    roomNumber
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    int number =
                            resultSet.getInt(
                                    "room_number"
                            );

                    String roomType =
                            resultSet.getString(
                                    "room_type"
                            );

                    double price =
                            resultSet.getDouble(
                                    "price"
                            );

                    boolean available =
                            resultSet.getInt(
                                    "available"
                            ) == 1;

                    return new Room(
                            number,
                            roomType,
                            price,
                            available
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =========================
    // UPDATE ROOM
    // =========================

    public void updateRoom(Room room) {

        String sql = """
                UPDATE rooms
                SET
                    room_type = ?,
                    price = ?,
                    available = ?
                WHERE room_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    room.getRoomType()
            );

            statement.setDouble(
                    2,
                    room.getPrice()
            );

            statement.setInt(
                    3,
                    room.isAvailable() ? 1 : 0
            );

            statement.setInt(
                    4,
                    room.getRoomNumber()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not update room.",
                    e
            );
        }
    }


    // =========================
    // DELETE ROOM
    // =========================

    public void deleteRoom(int roomNumber) {

        String sql = """
                DELETE FROM rooms
                WHERE room_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    roomNumber
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not delete room.",
                    e
            );
        }
    }


    // =========================
    // UPDATE ROOM AVAILABILITY
    // =========================

    public void updateAvailability(
            int roomNumber,
            boolean available
    ) {

        String sql = """
                UPDATE rooms
                SET available = ?
                WHERE room_number = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    available ? 1 : 0
            );

            statement.setInt(
                    2,
                    roomNumber
            );

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Could not update room availability.",
                    e
            );
        }
    }


    // =========================
    // SEARCH ROOMS
    // =========================

    public List<Room> searchRooms(
            String searchText
    ) {

        List<Room> rooms =
                new ArrayList<>();

        String sql = """
                SELECT
                    room_number,
                    room_type,
                    price,
                    available
                FROM rooms
                WHERE
                    CAST(room_number AS TEXT)
                    LIKE ?
                    OR room_type LIKE ?
                ORDER BY room_number
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

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    int roomNumber =
                            resultSet.getInt(
                                    "room_number"
                            );

                    String roomType =
                            resultSet.getString(
                                    "room_type"
                            );

                    double price =
                            resultSet.getDouble(
                                    "price"
                            );

                    boolean available =
                            resultSet.getInt(
                                    "available"
                            ) == 1;

                    Room room =
                            new Room(
                                    roomNumber,
                                    roomType,
                                    price,
                                    available
                            );

                    rooms.add(room);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return rooms;
    }
}