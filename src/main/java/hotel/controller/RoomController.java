package hotel.controller;

import hotel.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class RoomController {

    // =========================
    // Table and Columns
    // =========================

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    @FXML
    private TableColumn<Room, Double> priceColumn;

    @FXML
    private TableColumn<Room, Boolean> availableColumn;


    // =========================
    // Room List
    // =========================

    private ObservableList<Room> roomList =
            FXCollections.observableArrayList();


    // =========================
    // Initialize
    // =========================

    @FXML
    public void initialize() {

        // Connect columns with Room.java
        roomNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomNumber")
        );

        roomTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomType")
        );

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        availableColumn.setCellValueFactory(
                new PropertyValueFactory<>("available")
        );


        // Connect list with TableView
        roomTable.setItems(roomList);


        // Sample rooms for testing
        roomList.add(new Room(101, "Single", 1500, true));
        roomList.add(new Room(102, "Double", 2500, true));
        roomList.add(new Room(103, "Suite", 4000, false));
    }


    // =========================
    // ADD ROOM
    // =========================

    @FXML
    private void addRoom() {

        // Room Number
        TextInputDialog roomNumberDialog =
                new TextInputDialog();

        roomNumberDialog.setTitle("Add Room");
        roomNumberDialog.setHeaderText("Enter Room Number");
        roomNumberDialog.setContentText("Room Number:");

        Optional<String> roomNumberResult =
                roomNumberDialog.showAndWait();

        if (roomNumberResult.isEmpty()) {
            return;
        }


        // Room Type
        TextInputDialog roomTypeDialog =
                new TextInputDialog();

        roomTypeDialog.setTitle("Add Room");
        roomTypeDialog.setHeaderText("Enter Room Type");
        roomTypeDialog.setContentText("Room Type:");

        Optional<String> roomTypeResult =
                roomTypeDialog.showAndWait();

        if (roomTypeResult.isEmpty()) {
            return;
        }


        // Price
        TextInputDialog priceDialog =
                new TextInputDialog();

        priceDialog.setTitle("Add Room");
        priceDialog.setHeaderText("Enter Room Price");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult =
                priceDialog.showAndWait();

        if (priceResult.isEmpty()) {
            return;
        }


        try {

            int roomNumber =
                    Integer.parseInt(roomNumberResult.get());

            String roomType =
                    roomTypeResult.get();

            double price =
                    Double.parseDouble(priceResult.get());


            // New room is available by default
            Room room =
                    new Room(roomNumber, roomType, price, true);

            roomList.add(room);


            showMessage(
                    "Success",
                    "Room added successfully!"
            );

        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter valid numbers."
            );
        }
    }


    // =========================
    // UPDATE ROOM
    // =========================

    @FXML
    private void updateRoom() {

        Room selectedRoom =
                roomTable.getSelectionModel().getSelectedItem();


        if (selectedRoom == null) {

            showMessage(
                    "Warning",
                    "Please select a room first."
            );

            return;
        }


        // New Room Type
        TextInputDialog roomTypeDialog =
                new TextInputDialog(
                        selectedRoom.getRoomType()
                );

        roomTypeDialog.setTitle("Update Room");
        roomTypeDialog.setHeaderText(
                "Update Room Type"
        );
        roomTypeDialog.setContentText(
                "Room Type:"
        );


        Optional<String> roomTypeResult =
                roomTypeDialog.showAndWait();

        if (roomTypeResult.isEmpty()) {
            return;
        }


        // New Price
        TextInputDialog priceDialog =
                new TextInputDialog(
                        String.valueOf(
                                selectedRoom.getPrice()
                        )
                );

        priceDialog.setTitle("Update Room");
        priceDialog.setHeaderText(
                "Update Room Price"
        );
        priceDialog.setContentText(
                "Price:"
        );


        Optional<String> priceResult =
                priceDialog.showAndWait();

        if (priceResult.isEmpty()) {
            return;
        }


        try {

            String newRoomType =
                    roomTypeResult.get();

            double newPrice =
                    Double.parseDouble(
                            priceResult.get()
                    );


            selectedRoom.setRoomType(
                    newRoomType
            );

            selectedRoom.setPrice(
                    newPrice
            );


            roomTable.refresh();


            showMessage(
                    "Success",
                    "Room updated successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter a valid price."
            );
        }
    }


    // =========================
    // DELETE ROOM
    // =========================

    @FXML
    private void deleteRoom() {

        Room selectedRoom =
                roomTable.getSelectionModel().getSelectedItem();


        if (selectedRoom == null) {

            showMessage(
                    "Warning",
                    "Please select a room first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle("Delete Room");

        confirmation.setHeaderText(
                "Delete Room " +
                        selectedRoom.getRoomNumber() + "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this room?"
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            roomList.remove(selectedRoom);

            showMessage(
                    "Success",
                    "Room deleted successfully!"
            );
        }
    }


    // =========================
    // Show Message
    // =========================

    private void showMessage(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}