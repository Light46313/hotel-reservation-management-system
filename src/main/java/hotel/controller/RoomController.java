package hotel.controller;

import hotel.model.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class RoomController {

    // =========================
    // TABLE AND COLUMNS
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
    // PART 7 - SEARCH ROOM
    // =========================

    @FXML
    private TextField searchRoomField;


    // =========================
    // ROOM LIST
    // =========================

    private ObservableList<Room> roomList =
            FXCollections.observableArrayList();


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        // Connect Room Number column
        roomNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomNumber")
        );

        // Connect Room Type column
        roomTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomType")
        );

        // Connect Price column
        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        // Connect Available column
        availableColumn.setCellValueFactory(
                new PropertyValueFactory<>("available")
        );


        // Connect list with TableView
        roomTable.setItems(roomList);


        // =========================
        // HIGHLIGHT SELECTED ROOM
        // =========================

        roomTable.setRowFactory(tableView -> {

            TableRow<Room> row =
                    new TableRow<>();

            row.selectedProperty().addListener(
                    (observable, oldValue, newValue) -> {

                        if (newValue) {

                            // Highlight selected room
                            row.setStyle(
                                    "-fx-background-color: #FFD54F;"
                            );

                        } else {

                            // Remove custom highlight
                            row.setStyle("");
                        }
                    }
            );

            return row;
        });


        // =========================
        // SAMPLE ROOMS FOR TESTING
        // =========================

        roomList.add(
                new Room(101, "Single", 1500, true)
        );

        roomList.add(
                new Room(102, "Double", 2500, true)
        );

        roomList.add(
                new Room(103, "Suite", 4000, false)
        );


        // =========================
        // PART 7 - setOnAction()
        // =========================

        // Press Enter inside the TextField
        // to search for the room.

        searchRoomField.setOnAction(event -> {
            searchRoom();
        });
    }


    // =========================
    // PART 7 - SEARCH ROOM
    // =========================

    private void searchRoom() {

        // Get text from TextField

        String roomNumberText =
                searchRoomField.getText().trim();


        // Check if the TextField is empty

        if (roomNumberText.isEmpty()) {

            showMessage(
                    "Warning",
                    "Please enter a room number."
            );

            return;
        }


        try {

            // Convert entered text to integer

            int roomNumber =
                    Integer.parseInt(
                            roomNumberText
                    );


            // Search through all rooms

            for (Room room : roomList) {

                if (room.getRoomNumber() == roomNumber) {

                    // Select the found room

                    roomTable.getSelectionModel()
                            .select(room);


                    // Scroll to the found room

                    roomTable.scrollTo(room);


                    // Give focus to the TableView

                    roomTable.requestFocus();


                    showMessage(
                            "Room Found",
                            "Room "
                                    + roomNumber
                                    + " found successfully."
                    );

                    return;
                }
            }


            // Room was not found

            showMessage(
                    "Not Found",
                    "Room "
                            + roomNumber
                            + " was not found."
            );


        } catch (NumberFormatException e) {

            // Invalid input

            showMessage(
                    "Error",
                    "Please enter a valid room number."
            );
        }
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
        roomNumberDialog.setHeaderText(
                "Enter Room Number"
        );
        roomNumberDialog.setContentText(
                "Room Number:"
        );

        Optional<String> roomNumberResult =
                roomNumberDialog.showAndWait();

        if (roomNumberResult.isEmpty()) {
            return;
        }


        // Room Type
        TextInputDialog roomTypeDialog =
                new TextInputDialog();

        roomTypeDialog.setTitle("Add Room");
        roomTypeDialog.setHeaderText(
                "Enter Room Type"
        );
        roomTypeDialog.setContentText(
                "Room Type:"
        );

        Optional<String> roomTypeResult =
                roomTypeDialog.showAndWait();

        if (roomTypeResult.isEmpty()) {
            return;
        }


        // Price
        TextInputDialog priceDialog =
                new TextInputDialog();

        priceDialog.setTitle("Add Room");
        priceDialog.setHeaderText(
                "Enter Room Price"
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

            int roomNumber =
                    Integer.parseInt(
                            roomNumberResult.get()
                    );

            String roomType =
                    roomTypeResult.get();

            double price =
                    Double.parseDouble(
                            priceResult.get()
                    );


            // New room is available by default
            Room room =
                    new Room(
                            roomNumber,
                            roomType,
                            price,
                            true
                    );

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
                roomTable
                        .getSelectionModel()
                        .getSelectedItem();


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

        roomTypeDialog.setTitle(
                "Update Room"
        );

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

        priceDialog.setTitle(
                "Update Room"
        );

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


            // Refresh TableView
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
                roomTable
                        .getSelectionModel()
                        .getSelectedItem();


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

        confirmation.setTitle(
                "Delete Room"
        );

        confirmation.setHeaderText(
                "Delete Room "
                        + selectedRoom.getRoomNumber()
                        + "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this room?"
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            roomList.remove(
                    selectedRoom
            );

            showMessage(
                    "Success",
                    "Room deleted successfully!"
            );
        }
    }


    // =========================
    // BACK TO MAIN
    // =========================

    @FXML
    private void backToMain() {

        // Get the current Room Management window
        Stage stage =
                (Stage) roomTable
                        .getScene()
                        .getWindow();

        // Close Room Management window
        stage.close();
    }


    // =========================
    // SHOW MESSAGE
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