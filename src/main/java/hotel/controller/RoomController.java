package hotel.controller;

import hotel.database.RoomDAO;
import hotel.model.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
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
    // SEARCH ROOM
    // =========================

    @FXML
    private TextField searchRoomField;


    // =========================
    // FACILITY LIST
    // =========================

    @FXML
    private ListView<String> facilityListView;


    // =========================
    // TREEVIEW
    // =========================

    @FXML
    private TreeView<String> hotelTreeView;


    // =========================
    // ROOM LIST
    // =========================

    private final ObservableList<Room> roomList =
            FXCollections.observableArrayList();


    // =========================
    // ROOM DAO
    // =========================

    private final RoomDAO roomDAO =
            new RoomDAO();


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        // =========================
        // CONNECT TABLE COLUMNS
        // =========================

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


        // =========================
        // CONNECT LIST WITH TABLE
        // =========================

        roomTable.setItems(roomList);


        // =========================
        // LOAD ROOMS FROM DATABASE
        // =========================

        loadRoomsFromDatabase();


        // =========================
        // HIGHLIGHT SELECTED ROOM
        // =========================

        roomTable.setRowFactory(tableView -> {

            TableRow<Room> row =
                    new TableRow<>();

            row.selectedProperty().addListener(
                    (observable, oldValue, newValue) -> {

                        if (newValue) {

                            row.setStyle(
                                    "-fx-background-color: #FFD54F;"
                            );

                        } else {

                            row.setStyle("");
                        }
                    }
            );

            return row;
        });


        // =========================
        // SEARCH USING ENTER
        // =========================

        searchRoomField.setOnAction(event -> {
            searchRoom();
        });


        // =========================
        // FACILITY LIST
        // =========================

        ObservableList<String> facilities =
                FXCollections.observableArrayList(
                        "Swimming Pool",
                        "Restaurant",
                        "Gym",
                        "Parking",
                        "Wi-Fi"
                );

        facilityListView.setItems(facilities);


        // =========================
        // FACILITY SELECTION
        // =========================

        facilityListView.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue != null) {

                                showMessage(
                                        "Selected Facility",
                                        "Selected Facility: "
                                                + newValue
                                );
                            }
                        }
                );


        // =========================
        // TREEVIEW
        // =========================

        TreeItem<String> hotel =
                new TreeItem<>("Hotel");


        // =========================
        // ROOMS CATEGORY
        // =========================

        TreeItem<String> rooms =
                new TreeItem<>("Rooms");

        TreeItem<String> single =
                new TreeItem<>("Single");

        TreeItem<String> doubleRoom =
                new TreeItem<>("Double");

        TreeItem<String> suite =
                new TreeItem<>("Suite");


        // =========================
        // SERVICES CATEGORY
        // =========================

        TreeItem<String> services =
                new TreeItem<>("Services");

        TreeItem<String> restaurant =
                new TreeItem<>("Restaurant");

        TreeItem<String> gym =
                new TreeItem<>("Gym");

        TreeItem<String> swimmingPool =
                new TreeItem<>("Swimming Pool");


        // =========================
        // ADD ROOM TYPES
        // =========================

        rooms.getChildren().addAll(
                single,
                doubleRoom,
                suite
        );


        // =========================
        // ADD SERVICES
        // =========================

        services.getChildren().addAll(
                restaurant,
                gym,
                swimmingPool
        );


        // =========================
        // ADD CATEGORIES TO HOTEL
        // =========================

        hotel.getChildren().addAll(
                rooms,
                services
        );


        // =========================
        // SET TREE ROOT
        // =========================

        hotelTreeView.setRoot(hotel);


        // Expand Hotel
        hotel.setExpanded(true);

        // Expand Rooms
        rooms.setExpanded(true);

        // Expand Services
        services.setExpanded(true);


        // =========================
        // TREEVIEW SELECTION
        // =========================

        hotelTreeView.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue != null) {

                                showMessage(
                                        "Selected Node",
                                        "Selected: "
                                                + newValue.getValue()
                                );
                            }
                        }
                );
    }


    // =========================
    // LOAD ROOMS FROM DATABASE
    // =========================

    private void loadRoomsFromDatabase() {

        roomList.clear();

        List<Room> rooms =
                roomDAO.getAllRooms();

        roomList.addAll(rooms);
    }


    // =========================
    // SEARCH ROOM
    // =========================

    private void searchRoom() {

        String searchText =
                searchRoomField.getText().trim();


        // =========================
        // EMPTY SEARCH
        // =========================

        if (searchText.isEmpty()) {

            loadRoomsFromDatabase();

            return;
        }


        // =========================
        // SEARCH DATABASE
        // =========================

        List<Room> searchResults =
                roomDAO.searchRooms(searchText);


        roomList.setAll(searchResults);


        // =========================
        // CHECK RESULT
        // =========================

        if (searchResults.isEmpty()) {

            showMessage(
                    "Not Found",
                    "No room was found for: "
                            + searchText
            );

            return;
        }


        // =========================
        // SELECT FIRST RESULT
        // =========================

        Room firstRoom =
                searchResults.get(0);

        roomTable.getSelectionModel()
                .select(firstRoom);

        roomTable.scrollTo(firstRoom);

        roomTable.requestFocus();


        showMessage(
                "Room Found",
                "Room "
                        + firstRoom.getRoomNumber()
                        + " found successfully."
        );
    }


    // =========================
    // ADD ROOM
    // =========================

    @FXML
    private void addRoom() {

        // =========================
        // ROOM NUMBER
        // =========================

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


        // =========================
        // ROOM TYPE
        // =========================

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


        // =========================
        // PRICE
        // =========================

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
                            roomNumberResult.get().trim()
                    );

            String roomType =
                    roomTypeResult.get().trim();

            double price =
                    Double.parseDouble(
                            priceResult.get().trim()
                    );


            // =========================
            // VALIDATION
            // =========================

            if (roomType.isEmpty()) {

                showMessage(
                        "Error",
                        "Room type cannot be empty."
                );

                return;
            }

            if (price < 0) {

                showMessage(
                        "Error",
                        "Room price cannot be negative."
                );

                return;
            }


            // =========================
            // CHECK DUPLICATE ROOM
            // =========================

            Room existingRoom =
                    roomDAO.getRoomByNumber(roomNumber);

            if (existingRoom != null) {

                showMessage(
                        "Error",
                        "Room "
                                + roomNumber
                                + " already exists."
                );

                return;
            }


            // =========================
            // CREATE ROOM
            // =========================

            Room room =
                    new Room(
                            roomNumber,
                            roomType,
                            price,
                            true
                    );


            // =========================
            // SAVE TO DATABASE
            // =========================

            roomDAO.addRoom(room);


            // =========================
            // RELOAD FROM DATABASE
            // =========================

            loadRoomsFromDatabase();


            showMessage(
                    "Success",
                    "Room added successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter valid numbers."
            );

        } catch (RuntimeException e) {

            showMessage(
                    "Database Error",
                    e.getMessage()
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


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedRoom == null) {

            showMessage(
                    "Warning",
                    "Please select a room first."
            );

            return;
        }


        // =========================
        // NEW ROOM TYPE
        // =========================

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


        // =========================
        // NEW PRICE
        // =========================

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
                    roomTypeResult.get().trim();

            double newPrice =
                    Double.parseDouble(
                            priceResult.get().trim()
                    );


            // =========================
            // VALIDATION
            // =========================

            if (newRoomType.isEmpty()) {

                showMessage(
                        "Error",
                        "Room type cannot be empty."
                );

                return;
            }

            if (newPrice < 0) {

                showMessage(
                        "Error",
                        "Room price cannot be negative."
                );

                return;
            }


            // =========================
            // UPDATE OBJECT
            // =========================

            selectedRoom.setRoomType(
                    newRoomType
            );

            selectedRoom.setPrice(
                    newPrice
            );


            // =========================
            // UPDATE DATABASE
            // =========================

            roomDAO.updateRoom(
                    selectedRoom
            );


            // =========================
            // RELOAD DATABASE DATA
            // =========================

            loadRoomsFromDatabase();


            showMessage(
                    "Success",
                    "Room updated successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter a valid price."
            );

        } catch (RuntimeException e) {

            showMessage(
                    "Database Error",
                    e.getMessage()
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


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedRoom == null) {

            showMessage(
                    "Warning",
                    "Please select a room first."
            );

            return;
        }


        // =========================
        // CONFIRMATION
        // =========================

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


        // =========================
        // DELETE
        // =========================

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            try {

                roomDAO.deleteRoom(
                        selectedRoom.getRoomNumber()
                );


                loadRoomsFromDatabase();


                showMessage(
                        "Success",
                        "Room deleted successfully!"
                );

            } catch (RuntimeException e) {

                showMessage(
                        "Database Error",
                        e.getMessage()
                );
            }
        }
    }


    // =========================
    // BACK TO MAIN
    // =========================

    @FXML
    private void backToMain() {

        Stage stage =
                (Stage) roomTable
                        .getScene()
                        .getWindow();

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