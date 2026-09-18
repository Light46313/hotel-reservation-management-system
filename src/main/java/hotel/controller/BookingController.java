package hotel.controller;

import hotel.model.Booking;
import hotel.model.BookingData;
import hotel.model.Customer;
import hotel.model.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    // NEW BOOKING CONTROLS
    // =========================================

    // Customer ComboBox
    @FXML
    private ComboBox<Customer> customerComboBox;

    // Room ComboBox
    @FXML
    private ComboBox<Room> roomComboBox;

    // Check-in DatePicker
    @FXML
    private DatePicker checkInDatePicker;

    // Check-out DatePicker
    @FXML
    private DatePicker checkOutDatePicker;

    // Booking Type
    @FXML
    private RadioButton regularRadioButton;

    @FXML
    private RadioButton vipRadioButton;

    // Extra Services
    @FXML
    private CheckBox breakfastCheckBox;

    @FXML
    private CheckBox airportPickupCheckBox;


    // =========================================
    // NEW LISTS FOR COMBOBOX
    // =========================================

    private ObservableList<Customer> customerList =
            FXCollections.observableArrayList();

    private ObservableList<Room> roomList =
            FXCollections.observableArrayList();


    // =========================================
    // SELECTED CUSTOMER
    // =========================================

    // This stores the Customer object
    // received from CustomerController.
    private Customer selectedCustomer;

    @FXML
    private Label selectedCustomerLabel;


    // =========================================
    // INITIALIZE
    // =========================================

    @FXML
    public void initialize() {

        // -----------------------------------------
        // TABLE COLUMN CONNECTIONS
        // -----------------------------------------

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


        // -----------------------------------------
        // CONNECT SHARED BOOKING LIST
        // -----------------------------------------

        // Connect the SHARED list with the TableView.
        // BookingData.bookingList is the single source of
        // truth used by both Booking Management and
        // Booking History.
        bookingTable.setItems(
                BookingData.bookingList
        );


        // -----------------------------------------
        // LOAD SAMPLE DATA
        // -----------------------------------------

        // Only add sample data the first time the app runs
        // when the shared list is still empty.
        loadSampleDataIfEmpty();


        // =========================================
        // NEW: LOAD CUSTOMERS INTO COMBOBOX
        // =========================================

        loadCustomersIntoComboBox();


        // =========================================
        // NEW: LOAD ROOMS INTO COMBOBOX
        // =========================================

        loadRoomsIntoComboBox();


        // =========================================
        // NEW: DEFAULT BOOKING TYPE
        // =========================================

        // Select Regular by default.
        if (regularRadioButton != null
                && vipRadioButton != null) {

            if (!regularRadioButton.isSelected()
                    && !vipRadioButton.isSelected()) {

                regularRadioButton.setSelected(true);
            }
        }


        // =========================================
        // NEW: DATE FORMATTER
        // =========================================

        // The DatePicker will display dates as:
        // dd/MM/yyyy
        //
        // Example:
        // 18/09/2026

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");


        // -----------------------------------------
        // CHECK-IN DATE FORMATTER
        // -----------------------------------------

        checkInDatePicker.setConverter(
                new javafx.util.StringConverter<LocalDate>() {

                    @Override
                    public String toString(
                            LocalDate date) {

                        if (date == null) {
                            return "";
                        }

                        return formatter.format(date);
                    }


                    @Override
                    public LocalDate fromString(
                            String text) {

                        if (text == null
                                || text.trim().isEmpty()) {

                            return null;
                        }

                        return LocalDate.parse(
                                text,
                                formatter
                        );
                    }
                }
        );


        // -----------------------------------------
        // CHECK-OUT DATE FORMATTER
        // -----------------------------------------

        checkOutDatePicker.setConverter(
                new javafx.util.StringConverter<LocalDate>() {

                    @Override
                    public String toString(
                            LocalDate date) {

                        if (date == null) {
                            return "";
                        }

                        return formatter.format(date);
                    }


                    @Override
                    public LocalDate fromString(
                            String text) {

                        if (text == null
                                || text.trim().isEmpty()) {

                            return null;
                        }

                        return LocalDate.parse(
                                text,
                                formatter
                        );
                    }
                }
        );
    }


    // =========================================
    // NEW: LOAD CUSTOMERS
    // =========================================

    private void loadCustomersIntoComboBox() {

        customerList.clear();


        // Take customers from existing bookings.
        for (Booking booking :
                BookingData.bookingList) {

            Customer customer =
                    booking.getCustomer();

            if (customer != null
                    && !customerList.contains(customer)) {

                customerList.add(customer);
            }
        }


        // Connect list to ComboBox
        if (customerComboBox != null) {

            customerComboBox.setItems(
                    customerList
            );


            // Show customer name in ComboBox
            customerComboBox.setConverter(
                    new javafx.util.StringConverter<Customer>() {

                        @Override
                        public String toString(
                                Customer customer) {

                            if (customer == null) {
                                return "";
                            }

                            return customer.getName();
                        }


                        @Override
                        public Customer fromString(
                                String string) {

                            return null;
                        }
                    }
            );
        }
    }


    // =========================================
    // NEW: LOAD ROOMS
    // =========================================

    private void loadRoomsIntoComboBox() {

        roomList.clear();


        // Take rooms from existing bookings.
        for (Booking booking :
                BookingData.bookingList) {

            Room room =
                    booking.getRoom();

            if (room != null
                    && !roomList.contains(room)) {

                roomList.add(room);
            }
        }


        // Connect list to ComboBox
        if (roomComboBox != null) {

            roomComboBox.setItems(
                    roomList
            );


            // Show room number and type
            roomComboBox.setConverter(
                    new javafx.util.StringConverter<Room>() {

                        @Override
                        public String toString(
                                Room room) {

                            if (room == null) {
                                return "";
                            }

                            return "Room "
                                    + room.getRoomNumber()
                                    + " - "
                                    + room.getRoomType();
                        }


                        @Override
                        public Room fromString(
                                String string) {

                            return null;
                        }
                    }
            );
        }
    }


    // =========================================
    // PASS CUSTOMER DATA
    // =========================================

    // This method receives a Customer object
    // from CustomerController.
    public void setSelectedCustomer(
            Customer customer) {

        this.selectedCustomer = customer;


        // =========================================
        // NEW: ADD RECEIVED CUSTOMER TO COMBOBOX
        // =========================================

        if (customerComboBox != null) {

            // Add customer if it is not already there.
            if (!customerList.contains(customer)) {

                customerList.add(customer);
            }


            // Automatically select the received customer.
            customerComboBox.setValue(
                    customer
            );
        }


        // =========================================
        // SHOW SELECTED CUSTOMER
        // =========================================

        // Show the received customer's information
        // in the Booking Management screen.
        if (selectedCustomerLabel != null) {

            selectedCustomerLabel.setText(
                    "Selected Customer: ID = "
                            + customer.getCustomerId()
                            + " | Name = "
                            + customer.getName()
                            + " | Phone = "
                            + customer.getPhone()
            );
        }
    }


    // =========================================
    // SAMPLE DATA
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


        // =========================================
        // BOOKING ID
        // =========================================

        TextInputDialog bookingIdDialog =
                new TextInputDialog();

        bookingIdDialog.setTitle(
                "Add Booking"
        );

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


        try {

            // =========================================
            // BOOKING ID
            // =========================================

            int bookingId =
                    Integer.parseInt(
                            bookingIdResult.get()
                    );


            // =========================================
            // CUSTOMER
            // =========================================

            Customer customer;


            // If customer was passed from
            // Customer Management, use it.
            if (selectedCustomer != null) {

                customer = selectedCustomer;

            } else {

                // Otherwise use ComboBox.
                customer =
                        customerComboBox.getValue();


                if (customer == null) {

                    showMessage(
                            "Warning",
                            "Please select a customer."
                    );

                    return;
                }
            }


            // =========================================
            // ROOM
            // =========================================

            Room room =
                    roomComboBox.getValue();


            if (room == null) {

                showMessage(
                        "Warning",
                        "Please select a room."
                );

                return;
            }


            // =========================================
            // CHECK-IN DATE
            // =========================================

            LocalDate checkIn =
                    checkInDatePicker.getValue();


            if (checkIn == null) {

                showMessage(
                        "Warning",
                        "Please select check-in date."
                );

                return;
            }


            // =========================================
            // CHECK-OUT DATE
            // =========================================

            LocalDate checkOut =
                    checkOutDatePicker.getValue();


            if (checkOut == null) {

                showMessage(
                        "Warning",
                        "Please select check-out date."
                );

                return;
            }


            // =========================================
            // CHECK DATE
            // =========================================

            if (!checkOut.isAfter(checkIn)) {

                showMessage(
                        "Error",
                        "Check-out date must be after check-in date."
                );

                return;
            }


            // =========================================
            // BOOKING TYPE
            // =========================================

            String bookingType =
                    "Regular";


            if (vipRadioButton != null
                    && vipRadioButton.isSelected()) {

                bookingType = "VIP";
            }


            // =========================================
            // EXTRA SERVICES
            // =========================================

            String extraServices =
                    "None";


            boolean breakfast =
                    breakfastCheckBox != null
                            && breakfastCheckBox.isSelected();


            boolean airportPickup =
                    airportPickupCheckBox != null
                            && airportPickupCheckBox.isSelected();


            if (breakfast
                    && airportPickup) {

                extraServices =
                        "Breakfast, Airport Pickup";

            } else if (breakfast) {

                extraServices =
                        "Breakfast";

            } else if (airportPickup) {

                extraServices =
                        "Airport Pickup";
            }


            // =========================================
            // BOOKING OBJECT
            // =========================================

            Booking booking =
                    new Booking(
                            bookingId,
                            customer,
                            room,
                            checkIn,
                            checkOut
                    );


            // =========================================
            // ADD TO SHARED LIST
            // =========================================

            BookingData.bookingList.add(
                    booking
            );


            // =========================================
            // SUCCESS MESSAGE
            // =========================================

            showMessage(
                    "Success",
                    "Booking added successfully!\n\n"
                            + "Booking Type: "
                            + bookingType
                            + "\n"
                            + "Extra Services: "
                            + extraServices
            );


            // =========================================
            // CLEAR FORM
            // =========================================

            checkInDatePicker.setValue(null);

            checkOutDatePicker.setValue(null);

            if (regularRadioButton != null) {
                regularRadioButton.setSelected(true);
            }

            if (breakfastCheckBox != null) {
                breakfastCheckBox.setSelected(false);
            }

            if (airportPickupCheckBox != null) {
                airportPickupCheckBox.setSelected(false);
            }


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Please enter a valid Booking ID."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Error",
                    "Something went wrong while adding the booking."
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


        checkInDialog.setTitle(
                "Update Booking"
        );


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


        checkOutDialog.setTitle(
                "Update Booking"
        );


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
                "Delete Booking "
                        + selectedBooking.getBookingId()
                        + "?"
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