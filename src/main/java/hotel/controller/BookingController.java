package hotel.controller;

import hotel.model.Booking;
import hotel.model.BookingData;
import hotel.model.Customer;
import hotel.model.Room;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class BookingController {

    // =========================================
    // TABLE AND COLUMNS
    // =========================================

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, Customer> customerColumn;

    @FXML
    private TableColumn<Booking, Room> roomColumn;

    @FXML
    private TableColumn<Booking, LocalDate> checkInColumn;

    @FXML
    private TableColumn<Booking, LocalDate> checkOutColumn;


    // =========================================
    // INITIALIZE
    // =========================================

    @FXML
    public void initialize() {

        // Connect Booking ID column
        bookingIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookingId")
        );

        // Connect Customer column
        customerColumn.setCellValueFactory(
                new PropertyValueFactory<>("customer")
        );

        // Connect Room column
        roomColumn.setCellValueFactory(
                new PropertyValueFactory<>("room")
        );

        // Connect Check In column
        checkInColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkIn")
        );

        // Connect Check Out column
        checkOutColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkOut")
        );


        // Connect the SHARED list with the TableView.
        // BookingData.bookingList is the single source of
        // truth used by both Booking Management and
        // Booking History.
        bookingTable.setItems(BookingData.bookingList);


        // Only add sample data the first time the app runs
        // (when the shared list is still empty). This stops
        // duplicate sample bookings from being added every
        // time this screen is opened.
        loadSampleDataIfEmpty();
    }


    // =========================================
    // SAMPLE DATA (added only once)
    // =========================================

    private void loadSampleDataIfEmpty() {

        if (!BookingData.bookingList.isEmpty()) {
            return;
        }

        Customer customer1 =
                new Customer(
                        1,
                        "Argho",
                        "01711111111",
                        "argho@gmail.com",
                        "Dhaka"
                );

        Customer customer2 =
                new Customer(
                        2,
                        "Rahim",
                        "01822222222",
                        "rahim@gmail.com",
                        "Chittagong"
                );


        Room room1 =
                new Room(
                        101,
                        "Single",
                        1500,
                        true
                );

        Room room2 =
                new Room(
                        102,
                        "Double",
                        2500,
                        true
                );


        BookingData.bookingList.add(
                new Booking(
                        1,
                        customer1,
                        room1,
                        LocalDate.of(2026, 9, 20),
                        LocalDate.of(2026, 9, 23)
                )
        );


        BookingData.bookingList.add(
                new Booking(
                        2,
                        customer2,
                        room2,
                        LocalDate.of(2026, 9, 25),
                        LocalDate.of(2026, 9, 28)
                )
        );
    }


    // =========================================
    // ADD BOOKING
    // =========================================

    @FXML
    private void addBooking() {

        // -----------------------------------------
        // Booking ID
        // -----------------------------------------

        TextInputDialog bookingIdDialog =
                new TextInputDialog();

        bookingIdDialog.setTitle("Add Booking");
        bookingIdDialog.setHeaderText(
                "Enter Booking ID"
        );
        bookingIdDialog.setContentText(
                "Booking ID:"
        );

        Optional<String> bookingIdResult =
                bookingIdDialog.showAndWait();

        if (bookingIdResult.isEmpty()) {
            return;
        }


        // -----------------------------------------
        // Customer ID
        // -----------------------------------------

        TextInputDialog customerIdDialog =
                new TextInputDialog();

        customerIdDialog.setTitle("Add Booking");
        customerIdDialog.setHeaderText(
                "Enter Customer ID"
        );
        customerIdDialog.setContentText(
                "Customer ID:"
        );

        Optional<String> customerIdResult =
                customerIdDialog.showAndWait();

        if (customerIdResult.isEmpty()) {
            return;
        }


        // -----------------------------------------
        // Customer Name
        // -----------------------------------------

        TextInputDialog customerNameDialog =
                new TextInputDialog();

        customerNameDialog.setTitle("Add Booking");
        customerNameDialog.setHeaderText(
                "Enter Customer Name"
        );
        customerNameDialog.setContentText(
                "Customer Name:"
        );

        Optional<String> customerNameResult =
                customerNameDialog.showAndWait();

        if (customerNameResult.isEmpty()) {
            return;
        }


        // -----------------------------------------
        // Room Number
        // -----------------------------------------

        TextInputDialog roomNumberDialog =
                new TextInputDialog();

        roomNumberDialog.setTitle("Add Booking");
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


        // -----------------------------------------
        // Check In
        // -----------------------------------------

        TextInputDialog checkInDialog =
                new TextInputDialog();

        checkInDialog.setTitle("Add Booking");
        checkInDialog.setHeaderText(
                "Enter Check-In Date"
        );
        checkInDialog.setContentText(
                "Format: YYYY-MM-DD"
        );

        Optional<String> checkInResult =
                checkInDialog.showAndWait();

        if (checkInResult.isEmpty()) {
            return;
        }


        // -----------------------------------------
        // Check Out
        // -----------------------------------------

        TextInputDialog checkOutDialog =
                new TextInputDialog();

        checkOutDialog.setTitle("Add Booking");
        checkOutDialog.setHeaderText(
                "Enter Check-Out Date"
        );
        checkOutDialog.setContentText(
                "Format: YYYY-MM-DD"
        );

        Optional<String> checkOutResult =
                checkOutDialog.showAndWait();

        if (checkOutResult.isEmpty()) {
            return;
        }


        // =========================================
        // CREATE BOOKING
        // =========================================

        try {

            int bookingId =
                    Integer.parseInt(
                            bookingIdResult.get()
                    );

            int customerId =
                    Integer.parseInt(
                            customerIdResult.get()
                    );

            int roomNumber =
                    Integer.parseInt(
                            roomNumberResult.get()
                    );


            String customerName =
                    customerNameResult.get();


            LocalDate checkIn =
                    LocalDate.parse(
                            checkInResult.get()
                    );

            LocalDate checkOut =
                    LocalDate.parse(
                            checkOutResult.get()
                    );


            // Check date
            if (!checkOut.isAfter(checkIn)) {

                showMessage(
                        "Error",
                        "Check-out date must be after check-in date."
                );

                return;
            }


            // Create Customer object
            Customer customer =
                    new Customer(
                            customerId,
                            customerName,
                            "N/A",
                            "N/A",
                            "N/A"
                    );


            // Create Room object
            Room room =
                    new Room(
                            roomNumber,
                            "Standard",
                            0,
                            true
                    );


            // Create Booking object
            Booking booking =
                    new Booking(
                            bookingId,
                            customer,
                            room,
                            checkIn,
                            checkOut
                    );


            // Add booking to the SHARED list
            BookingData.bookingList.add(booking);


            showMessage(
                    "Success",
                    "Booking added successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter valid numbers."
            );

        } catch (Exception e) {

            showMessage(
                    "Error",
                    "Please enter dates in YYYY-MM-DD format."
            );
        }
    }


    // =========================================
    // UPDATE BOOKING
    // =========================================

    @FXML
    private void updateBooking() {

        Booking selectedBooking =
                bookingTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedBooking == null) {

            showMessage(
                    "Warning",
                    "Please select a booking first."
            );

            return;
        }


        // -----------------------------------------
        // New Check-In Date
        // -----------------------------------------

        TextInputDialog checkInDialog =
                new TextInputDialog(
                        selectedBooking
                                .getCheckIn()
                                .toString()
                );

        checkInDialog.setTitle("Update Booking");
        checkInDialog.setHeaderText(
                "Update Check-In Date"
        );
        checkInDialog.setContentText(
                "YYYY-MM-DD:"
        );


        Optional<String> checkInResult =
                checkInDialog.showAndWait();


        if (checkInResult.isEmpty()) {
            return;
        }


        // -----------------------------------------
        // New Check-Out Date
        // -----------------------------------------

        TextInputDialog checkOutDialog =
                new TextInputDialog(
                        selectedBooking
                                .getCheckOut()
                                .toString()
                );

        checkOutDialog.setTitle("Update Booking");
        checkOutDialog.setHeaderText(
                "Update Check-Out Date"
        );
        checkOutDialog.setContentText(
                "YYYY-MM-DD:"
        );


        Optional<String> checkOutResult =
                checkOutDialog.showAndWait();


        if (checkOutResult.isEmpty()) {
            return;
        }


        try {

            LocalDate newCheckIn =
                    LocalDate.parse(
                            checkInResult.get()
                    );

            LocalDate newCheckOut =
                    LocalDate.parse(
                            checkOutResult.get()
                    );


            if (!newCheckOut.isAfter(newCheckIn)) {

                showMessage(
                        "Error",
                        "Check-out date must be after check-in date."
                );

                return;
            }


            selectedBooking.setCheckIn(
                    newCheckIn
            );

            selectedBooking.setCheckOut(
                    newCheckOut
            );


            bookingTable.refresh();


            showMessage(
                    "Success",
                    "Booking updated successfully!"
            );


        } catch (Exception e) {

            showMessage(
                    "Error",
                    "Please enter dates in YYYY-MM-DD format."
            );
        }
    }


    // =========================================
    // DELETE BOOKING
    // =========================================

    @FXML
    private void deleteBooking() {

        Booking selectedBooking =
                bookingTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedBooking == null) {

            showMessage(
                    "Warning",
                    "Please select a booking first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Delete Booking"
        );

        confirmation.setHeaderText(
                "Delete Booking " +
                        selectedBooking.getBookingId() +
                        "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this booking?"
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            BookingData.bookingList.remove(
                    selectedBooking
            );


            showMessage(
                    "Success",
                    "Booking deleted successfully!"
            );
        }
    }


    // =========================================
    // BACK TO MAIN
    // =========================================

    @FXML
    private void backToMain() {

        Stage stage =
                (Stage) bookingTable
                        .getScene()
                        .getWindow();

        stage.close();
    }


    // =========================================
    // COMMON MESSAGE
    // =========================================

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