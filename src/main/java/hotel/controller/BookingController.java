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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
    // PART 11 CONTROLS
    // =========================================

    // Booking progress
    @FXML
    private ProgressBar bookingProgressBar;

    // Room price range
    @FXML
    private Slider roomPriceSlider;

    @FXML
    private Label roomPriceLabel;

    // Number of guests
    @FXML
    private Spinner<Integer> guestSpinner;


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
        // LOAD CUSTOMERS INTO COMBOBOX
        // =========================================

        loadCustomersIntoComboBox();


        // =========================================
        // UPDATE SELECTED CUSTOMER LABEL
        // =========================================

        // When a customer is selected directly
        // from the Customer ComboBox, update the
        // selected customer and the label at the top.
        if (customerComboBox != null) {

            customerComboBox.valueProperty()
                    .addListener(
                            (observable, oldCustomer, newCustomer) -> {

                                selectedCustomer = newCustomer;

                                if (selectedCustomerLabel != null) {

                                    if (newCustomer != null) {

                                        selectedCustomerLabel.setText(
                                                "Selected Customer: ID = "
                                                        + newCustomer.getCustomerId()
                                                        + " | Name = "
                                                        + newCustomer.getName()
                                                        + " | Phone = "
                                                        + newCustomer.getPhone()
                                        );

                                    } else {

                                        selectedCustomerLabel.setText(
                                                "No customer selected"
                                        );
                                    }
                                }

                                // Update ProgressBar after
                                // customer selection.
                                updateBookingProgress();
                            }
                    );
        }


        // =========================================
        // LOAD ROOMS INTO COMBOBOX
        // =========================================

        loadRoomsIntoComboBox();


        // =========================================
        // DEFAULT BOOKING TYPE
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
        // DATE FORMATTER
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


        // =========================================
        // PART 11 - PROGRESS BAR
        // =========================================

        setupBookingProgress();


        // =========================================
        // PART 11 - ROOM PRICE SLIDER
        // =========================================

        setupRoomPriceSlider();


        // =========================================
        // PART 11 - GUEST SPINNER
        // =========================================

        setupGuestSpinner();
    }


    // =========================================
    // PART 13 - WARNING ALERT
    // =========================================

    private void showWarning(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================================
    // PART 13 - ERROR ALERT
    // =========================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================================
    // PART 11 - PROGRESS BAR SETUP
    // =========================================

    private void setupBookingProgress() {

        if (bookingProgressBar == null) {
            return;
        }

        // Start from 0%.
        bookingProgressBar.setProgress(0);


        // Customer selected
        if (customerComboBox != null) {

            customerComboBox.valueProperty()
                    .addListener(
                            (observable, oldValue, newValue) -> {
                                updateBookingProgress();
                            }
                    );
        }


        // Room selected
        if (roomComboBox != null) {

            roomComboBox.valueProperty()
                    .addListener(
                            (observable, oldValue, newValue) -> {
                                updateBookingProgress();
                            }
                    );
        }


        // Check-in selected
        if (checkInDatePicker != null) {

            checkInDatePicker.valueProperty()
                    .addListener(
                            (observable, oldValue, newValue) -> {
                                updateBookingProgress();
                            }
                    );
        }


        // Check-out selected
        if (checkOutDatePicker != null) {

            checkOutDatePicker.valueProperty()
                    .addListener(
                            (observable, oldValue, newValue) -> {
                                updateBookingProgress();
                            }
                    );
        }


        // Initial progress
        updateBookingProgress();
    }


    // =========================================
    // PART 11 - UPDATE PROGRESS
    // =========================================

    private void updateBookingProgress() {

        if (bookingProgressBar == null) {
            return;
        }

        double progress = 0.0;


        // Step 1 - Customer
        Customer customer = null;

        if (selectedCustomer != null) {
            customer = selectedCustomer;

        } else if (customerComboBox != null) {
            customer = customerComboBox.getValue();
        }


        if (customer != null) {
            progress = 0.25;
        }


        // Step 2 - Room
        if (roomComboBox != null
                && roomComboBox.getValue() != null) {

            progress = 0.50;
        }


        // Step 3 - Check-in date
        if (checkInDatePicker != null
                && checkInDatePicker.getValue() != null) {

            progress = 0.75;
        }


        // Step 4 - Check-out date
        if (checkOutDatePicker != null
                && checkOutDatePicker.getValue() != null) {

            progress = 1.0;
        }


        bookingProgressBar.setProgress(progress);
    }


    // =========================================
    // PART 11 - ROOM PRICE SLIDER
    // =========================================

    private void setupRoomPriceSlider() {

        if (roomPriceSlider == null) {
            return;
        }

        // Minimum room price
        roomPriceSlider.setMin(1000);

        // Maximum room price
        roomPriceSlider.setMax(5000);

        // Starting price
        roomPriceSlider.setValue(5000);

        // Show the starting price
        if (roomPriceLabel != null) {

            roomPriceLabel.setText(
                    "Room Price Range: 5000"
            );
        }

        // Change price label and rooms when slider moves
        roomPriceSlider.valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            double price =
                                    newValue.doubleValue();

                            // Show current price
                            if (roomPriceLabel != null) {

                                roomPriceLabel.setText(
                                        "Room Price Range: "
                                                + (int) price
                                );
                            }

                            // Filter rooms
                            filterRoomsByPrice(price);
                        }
                );
    }


    // =========================================
    // PART 11 - FILTER ROOMS BY PRICE
    // =========================================

    private void filterRoomsByPrice(
            double maximumPrice) {

        if (roomComboBox == null) {
            return;
        }

        ObservableList<Room> filteredRooms =
                FXCollections.observableArrayList();


        for (Room room : roomList) {

            if (room.getPrice() <= maximumPrice) {

                filteredRooms.add(room);
            }
        }


        roomComboBox.setItems(filteredRooms);
    }


    // =========================================
    // PART 11 - GUEST SPINNER
    // =========================================

    private void setupGuestSpinner() {

        if (guestSpinner == null) {
            return;
        }

        // Minimum = 1 guest
        // Maximum = 10 guests
        // Starting value = 1 guest

        guestSpinner.setValueFactory(
                new SpinnerValueFactory
                        .IntegerSpinnerValueFactory(
                        1,
                        10,
                        1
                )
        );
    }


    // =========================================
    // LOAD CUSTOMERS
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
    // LOAD ROOMS
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
        // ADD RECEIVED CUSTOMER TO COMBOBOX
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


        // Update progress after customer selection.
        updateBookingProgress();
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

                    // PART 13 - WARNING
                    showWarning(
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

                // PART 13 - WARNING
                showWarning(
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

                // PART 13 - WARNING
                showWarning(
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

                // PART 13 - WARNING
                showWarning(
                        "Warning",
                        "Please select check-out date."
                );

                return;
            }


            // =========================================
            // CHECK DATE
            // =========================================

            if (!checkOut.isAfter(checkIn)) {

                // PART 13 - ERROR
                showError(
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
            // NUMBER OF GUESTS
            // =========================================

            int numberOfGuests = 1;

            if (guestSpinner != null
                    && guestSpinner.getValue() != null) {

                numberOfGuests =
                        guestSpinner.getValue();
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
                            + "\n"
                            + "Number of Guests: "
                            + numberOfGuests
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


            // Reset guest count to 1.
            if (guestSpinner != null
                    && guestSpinner.getValueFactory() != null) {

                guestSpinner.getValueFactory()
                        .setValue(1);
            }


            // Update progress after clearing dates.
            updateBookingProgress();


        } catch (NumberFormatException e) {

            // PART 13 - ERROR
            showError(
                    "Error",
                    "Please enter a valid Booking ID."
            );

        } catch (Exception e) {

            e.printStackTrace();

            // PART 13 - ERROR
            showError(
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

            // PART 13 - WARNING
            showWarning(
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

                // PART 13 - ERROR
                showError(
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


            // Existing INFORMATION alert
            showMessage(
                    "Success",
                    "Booking updated successfully!"
            );


        } catch (Exception e) {

            // PART 13 - ERROR
            showError(
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

            // PART 13 - WARNING
            showWarning(
                    "Warning",
                    "Please select a booking first."
            );

            return;
        }


        // =========================================
        // CONFIRMATION ALERT
        // =========================================

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


            // Existing INFORMATION alert
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
    // COMMON INFORMATION MESSAGE
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