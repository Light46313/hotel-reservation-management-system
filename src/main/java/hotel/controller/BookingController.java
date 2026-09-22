package hotel.controller;

import hotel.model.Booking;
import hotel.model.BookingData;
import hotel.model.Customer;
import hotel.model.Room;
import hotel.service.BookingService;

import javafx.application.Platform;

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

import java.util.concurrent.Future;


public class BookingController {

    // =========================================================
    // BOOKING SERVICE
    // =========================================================

    /*
     * BookingService handles the booking process
     * using multithreading.
     */
    private final BookingService bookingService =
            new BookingService();


    // =========================================================
    // TABLE AND COLUMNS
    // =========================================================

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


    // =========================================================
    // BOOKING INPUT CONTROLS
    // =========================================================

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Room> roomComboBox;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;


    // =========================================================
    // BOOKING TYPE
    // =========================================================

    @FXML
    private RadioButton regularRadioButton;

    @FXML
    private RadioButton vipRadioButton;


    // =========================================================
    // EXTRA SERVICES
    // =========================================================

    @FXML
    private CheckBox breakfastCheckBox;

    @FXML
    private CheckBox airportPickupCheckBox;


    // =========================================================
    // PART 11 CONTROLS
    // =========================================================

    @FXML
    private ProgressBar bookingProgressBar;

    @FXML
    private Slider roomPriceSlider;

    @FXML
    private Label roomPriceLabel;

    @FXML
    private Spinner<Integer> guestSpinner;


    // =========================================================
    // LISTS
    // =========================================================

    private ObservableList<Customer> customerList =
            FXCollections.observableArrayList();

    private ObservableList<Room> roomList =
            FXCollections.observableArrayList();


    // =========================================================
    // SELECTED CUSTOMER
    // =========================================================

    private Customer selectedCustomer;

    @FXML
    private Label selectedCustomerLabel;


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        // ---------------------------------------------------------
        // TABLE COLUMN CONNECTIONS
        // ---------------------------------------------------------

        bookingIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookingId")
        );

        customerColumn.setCellValueFactory(
                new PropertyValueFactory<>("customer")
        );

        roomColumn.setCellValueFactory(
                new PropertyValueFactory<>("room")
        );

        checkInColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkIn")
        );

        checkOutColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkOut")
        );


        // ---------------------------------------------------------
        // SHARED BOOKING LIST
        // ---------------------------------------------------------

        bookingTable.setItems(
                BookingData.bookingList
        );


        // ---------------------------------------------------------
        // LOAD SAMPLE DATA
        // ---------------------------------------------------------

        loadSampleDataIfEmpty();


        // ---------------------------------------------------------
        // LOAD CUSTOMERS
        // ---------------------------------------------------------

        loadCustomersIntoComboBox();


        // ---------------------------------------------------------
        // CUSTOMER COMBOBOX LISTENER
        // ---------------------------------------------------------

        if (customerComboBox != null) {

            customerComboBox.valueProperty()
                    .addListener(
                            (observable,
                             oldCustomer,
                             newCustomer) -> {

                                selectedCustomer =
                                        newCustomer;

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

                                updateBookingProgress();
                            }
                    );
        }


        // ---------------------------------------------------------
        // LOAD ROOMS
        // ---------------------------------------------------------

        loadRoomsIntoComboBox();


        // ---------------------------------------------------------
        // DEFAULT BOOKING TYPE
        // ---------------------------------------------------------

        if (regularRadioButton != null
                && vipRadioButton != null) {

            if (!regularRadioButton.isSelected()
                    && !vipRadioButton.isSelected()) {

                regularRadioButton.setSelected(true);
            }
        }


        // ---------------------------------------------------------
        // DATE FORMATTER
        // ---------------------------------------------------------

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );


        // ---------------------------------------------------------
        // CHECK-IN DATE FORMATTER
        // ---------------------------------------------------------

        if (checkInDatePicker != null) {

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
        }


        // ---------------------------------------------------------
        // CHECK-OUT DATE FORMATTER
        // ---------------------------------------------------------

        if (checkOutDatePicker != null) {

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


        // ---------------------------------------------------------
        // PROGRESS BAR
        // ---------------------------------------------------------

        setupBookingProgress();


        // ---------------------------------------------------------
        // ROOM PRICE SLIDER
        // ---------------------------------------------------------

        setupRoomPriceSlider();


        // ---------------------------------------------------------
        // GUEST SPINNER
        // ---------------------------------------------------------

        setupGuestSpinner();
    }


    // =========================================================
    // WARNING MESSAGE
    // =========================================================

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================================================
    // PART 11 - PROGRESS BAR
    // =========================================================

    private void setupBookingProgress() {

        if (bookingProgressBar == null) {
            return;
        }

        // Start at 0%
        bookingProgressBar.setProgress(0);


        // Customer selected
        if (customerComboBox != null) {

            customerComboBox.valueProperty()
                    .addListener(
                            (observable,
                             oldValue,
                             newValue) -> {

                                updateBookingProgress();
                            }
                    );
        }


        // Room selected
        if (roomComboBox != null) {

            roomComboBox.valueProperty()
                    .addListener(
                            (observable,
                             oldValue,
                             newValue) -> {

                                updateBookingProgress();
                            }
                    );
        }


        // Check-in selected
        if (checkInDatePicker != null) {

            checkInDatePicker.valueProperty()
                    .addListener(
                            (observable,
                             oldValue,
                             newValue) -> {

                                updateBookingProgress();
                            }
                    );
        }


        // Check-out selected
        if (checkOutDatePicker != null) {

            checkOutDatePicker.valueProperty()
                    .addListener(
                            (observable,
                             oldValue,
                             newValue) -> {

                                updateBookingProgress();
                            }
                    );
        }


        updateBookingProgress();
    }


    // =========================================================
    // UPDATE PROGRESS BAR
    // =========================================================

    private void updateBookingProgress() {

        if (bookingProgressBar == null) {
            return;
        }

        double progress = 0.0;


        // ---------------------------------------------------------
        // STEP 1 - CUSTOMER
        // ---------------------------------------------------------

        Customer customer = null;

        if (selectedCustomer != null) {

            customer = selectedCustomer;

        } else if (customerComboBox != null) {

            customer =
                    customerComboBox.getValue();
        }

        if (customer != null) {

            progress = 0.25;
        }


        // ---------------------------------------------------------
        // STEP 2 - ROOM
        // ---------------------------------------------------------

        if (roomComboBox != null
                && roomComboBox.getValue() != null) {

            progress = 0.50;
        }


        // ---------------------------------------------------------
        // STEP 3 - CHECK-IN
        // ---------------------------------------------------------

        if (checkInDatePicker != null
                && checkInDatePicker.getValue() != null) {

            progress = 0.75;
        }


        // ---------------------------------------------------------
        // STEP 4 - CHECK-OUT
        // ---------------------------------------------------------

        if (checkOutDatePicker != null
                && checkOutDatePicker.getValue() != null) {

            progress = 1.0;
        }


        bookingProgressBar.setProgress(progress);
    }


    // =========================================================
    // PART 11 - ROOM PRICE SLIDER
    // =========================================================

    private void setupRoomPriceSlider() {

        if (roomPriceSlider == null) {
            return;
        }


        // Minimum price
        roomPriceSlider.setMin(1000);


        // Maximum price
        roomPriceSlider.setMax(5000);


        // Starting value
        roomPriceSlider.setValue(5000);


        // Display starting price
        if (roomPriceLabel != null) {

            roomPriceLabel.setText(
                    "Room Price Range: 5000"
            );
        }


        // Slider listener
        roomPriceSlider.valueProperty()
                .addListener(
                        (observable,
                         oldValue,
                         newValue) -> {

                            double price =
                                    newValue.doubleValue();


                            // Update label
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


    // =========================================================
    // FILTER ROOMS BY PRICE
    // =========================================================

    private void filterRoomsByPrice(
            double maximumPrice) {

        if (roomComboBox == null) {
            return;
        }


        ObservableList<Room> filteredRooms =
                FXCollections.observableArrayList();


        for (Room room : roomList) {

            if (room.getPrice()
                    <= maximumPrice) {

                filteredRooms.add(room);
            }
        }


        roomComboBox.setItems(
                filteredRooms
        );
    }


    // =========================================================
    // PART 11 - GUEST SPINNER
    // =========================================================

    private void setupGuestSpinner() {

        if (guestSpinner == null) {
            return;
        }


        /*
         * Minimum = 1
         * Maximum = 10
         * Initial value = 1
         */

        guestSpinner.setValueFactory(
                new SpinnerValueFactory
                        .IntegerSpinnerValueFactory(
                        1,
                        10,
                        1
                )
        );
    }


    // =========================================================
    // LOAD CUSTOMERS
    // =========================================================

    private void loadCustomersIntoComboBox() {

        customerList.clear();


        /*
         * Take customers from existing bookings.
         */

        for (Booking booking :
                BookingData.bookingList) {

            Customer customer =
                    booking.getCustomer();

            if (customer != null
                    && !customerList.contains(customer)) {

                customerList.add(customer);
            }
        }


        if (customerComboBox != null) {

            customerComboBox.setItems(
                    customerList
            );


            // Show customer name
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


    // =========================================================
    // LOAD ROOMS
    // =========================================================

    private void loadRoomsIntoComboBox() {

        roomList.clear();


        /*
         * Take rooms from existing bookings.
         */

        for (Booking booking :
                BookingData.bookingList) {

            Room room =
                    booking.getRoom();

            if (room != null
                    && !roomList.contains(room)) {

                roomList.add(room);
            }
        }


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


    // =========================================================
    // PASS CUSTOMER DATA
    // =========================================================

    /*
     * This method receives a Customer object
     * from CustomerController.
     */

    public void setSelectedCustomer(
            Customer customer) {

        this.selectedCustomer = customer;


        // Add customer to ComboBox
        if (customerComboBox != null) {

            if (!customerList.contains(customer)) {

                customerList.add(customer);
            }

            customerComboBox.setValue(customer);
        }


        // Show selected customer
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


        updateBookingProgress();
    }


    // =========================================================
    // SAMPLE DATA
    // =========================================================

    private void loadSampleDataIfEmpty() {

        if (!BookingData.bookingList.isEmpty()) {
            return;
        }


        // ---------------------------------------------------------
        // CUSTOMER 1
        // ---------------------------------------------------------

        Customer customer1 =
                new Customer(
                        1,
                        "Argho",
                        "01711111111",
                        "argho@gmail.com",
                        "Dhaka"
                );


        // ---------------------------------------------------------
        // CUSTOMER 2
        // ---------------------------------------------------------

        Customer customer2 =
                new Customer(
                        2,
                        "Rahim",
                        "01822222222",
                        "rahim@gmail.com",
                        "Chittagong"
                );


        // ---------------------------------------------------------
        // ROOM 1
        // ---------------------------------------------------------

        Room room1 =
                new Room(
                        101,
                        "Single",
                        1500,
                        true
                );


        // ---------------------------------------------------------
        // ROOM 2
        // ---------------------------------------------------------

        Room room2 =
                new Room(
                        102,
                        "Double",
                        2500,
                        true
                );


        // ---------------------------------------------------------
        // SAMPLE BOOKING 1
        // ---------------------------------------------------------

        BookingData.bookingList.add(
                new Booking(
                        1,
                        customer1,
                        room1,
                        LocalDate.of(2026, 9, 20),
                        LocalDate.of(2026, 9, 23)
                )
        );


        // ---------------------------------------------------------
        // SAMPLE BOOKING 2
        // ---------------------------------------------------------

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


// =========================================================
// END OF FIRST HALF
// =========================================================

/*
 * The next part will start here with:
 *
 * @FXML
 * private void addBooking()
 *
 * In that method the multithreading code will use
 * finalBookingType,
 * finalExtraServices,
 * finalNumberOfGuests
 * so the lambda errors are removed.
 */
    // =========================================================
    // ADD BOOKING
    // =========================================================

    @FXML
    private void addBooking() {

        // ---------------------------------------------------------
        // BOOKING ID
        // ---------------------------------------------------------

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

            // ---------------------------------------------------------
            // BOOKING ID
            // ---------------------------------------------------------

            int bookingId =
                    Integer.parseInt(
                            bookingIdResult.get()
                    );


            // ---------------------------------------------------------
            // CUSTOMER
            // ---------------------------------------------------------

            Customer customer;

            if (selectedCustomer != null) {

                customer = selectedCustomer;

            } else {

                customer =
                        customerComboBox.getValue();

                if (customer == null) {

                    showWarning(
                            "Warning",
                            "Please select a customer."
                    );

                    return;
                }
            }


            // ---------------------------------------------------------
            // ROOM
            // ---------------------------------------------------------

            Room room =
                    roomComboBox.getValue();

            if (room == null) {

                showWarning(
                        "Warning",
                        "Please select a room."
                );

                return;
            }


            // ---------------------------------------------------------
            // CHECK-IN DATE
            // ---------------------------------------------------------

            LocalDate checkIn =
                    checkInDatePicker.getValue();

            if (checkIn == null) {

                showWarning(
                        "Warning",
                        "Please select check-in date."
                );

                return;
            }


            // ---------------------------------------------------------
            // CHECK-OUT DATE
            // ---------------------------------------------------------

            LocalDate checkOut =
                    checkOutDatePicker.getValue();

            if (checkOut == null) {

                showWarning(
                        "Warning",
                        "Please select check-out date."
                );

                return;
            }


            // ---------------------------------------------------------
            // DATE VALIDATION
            // ---------------------------------------------------------

            if (!checkOut.isAfter(checkIn)) {

                showError(
                        "Error",
                        "Check-out date must be after check-in date."
                );

                return;
            }


            // ---------------------------------------------------------
            // BOOKING TYPE
            // ---------------------------------------------------------

            String bookingType =
                    "Regular";

            if (vipRadioButton != null
                    && vipRadioButton.isSelected()) {

                bookingType = "VIP";
            }


            // ---------------------------------------------------------
            // EXTRA SERVICES
            // ---------------------------------------------------------

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


            // ---------------------------------------------------------
            // NUMBER OF GUESTS
            // ---------------------------------------------------------

            int numberOfGuests = 1;

            if (guestSpinner != null
                    && guestSpinner.getValue() != null) {

                numberOfGuests =
                        guestSpinner.getValue();
            }


            // =========================================================
            // IMPORTANT FIX FOR LAMBDA
            // =========================================================
            /*
             * Variables used inside Platform.runLater()
             * must be final or effectively final.
             *
             * Therefore we create final copies here.
             */

            final String finalBookingType =
                    bookingType;

            final String finalExtraServices =
                    extraServices;

            final int finalNumberOfGuests =
                    numberOfGuests;


            // =========================================================
            // MULTITHREADING
            // =========================================================

            /*
             * BookingService processes the booking
             * using ExecutorService.
             *
             * Therefore the booking task is performed
             * on a worker thread instead of directly
             * on the JavaFX Application Thread.
             */

            Future<Booking> future =
                    bookingService.createBookingAsync(
                            bookingId,
                            customer,
                            room,
                            checkIn,
                            checkOut
                    );


            // ---------------------------------------------------------
            // WAIT FOR RESULT IN SEPARATE THREAD
            // ---------------------------------------------------------

            Thread resultThread =
                    new Thread(() -> {

                        try {

                            /*
                             * Wait for BookingService to finish.
                             */
                            Booking booking =
                                    future.get();


                            /*
                             * JavaFX controls must be modified
                             * on the JavaFX Application Thread.
                             */

                            Platform.runLater(() -> {

                                if (booking != null) {

                                    // -------------------------------------------------
                                    // ADD BOOKING TO SHARED LIST
                                    // -------------------------------------------------

                                    BookingData.bookingList.add(
                                            booking
                                    );


                                    // -------------------------------------------------
                                    // SUCCESS MESSAGE
                                    // -------------------------------------------------

                                    showMessage(
                                            "Success",
                                            "Booking added successfully!\n\n"
                                                    + "Booking Type: "
                                                    + finalBookingType
                                                    + "\n"
                                                    + "Extra Services: "
                                                    + finalExtraServices
                                                    + "\n"
                                                    + "Number of Guests: "
                                                    + finalNumberOfGuests
                                    );


                                    // -------------------------------------------------
                                    // CLEAR DATE FIELDS
                                    // -------------------------------------------------

                                    checkInDatePicker.setValue(
                                            null
                                    );

                                    checkOutDatePicker.setValue(
                                            null
                                    );


                                    // -------------------------------------------------
                                    // RESET BOOKING TYPE
                                    // -------------------------------------------------

                                    if (regularRadioButton != null) {

                                        regularRadioButton.setSelected(
                                                true
                                        );
                                    }


                                    // -------------------------------------------------
                                    // RESET BREAKFAST
                                    // -------------------------------------------------

                                    if (breakfastCheckBox != null) {

                                        breakfastCheckBox.setSelected(
                                                false
                                        );
                                    }


                                    // -------------------------------------------------
                                    // RESET AIRPORT PICKUP
                                    // -------------------------------------------------

                                    if (airportPickupCheckBox != null) {

                                        airportPickupCheckBox.setSelected(
                                                false
                                        );
                                    }


                                    // -------------------------------------------------
                                    // RESET GUEST SPINNER
                                    // -------------------------------------------------

                                    if (guestSpinner != null
                                            && guestSpinner
                                            .getValueFactory() != null) {

                                        guestSpinner
                                                .getValueFactory()
                                                .setValue(1);
                                    }


                                    // -------------------------------------------------
                                    // UPDATE PROGRESS
                                    // -------------------------------------------------

                                    updateBookingProgress();
                                }
                            });


                        } catch (Exception e) {

                            e.printStackTrace();

                            /*
                             * Show error on JavaFX Application Thread.
                             */

                            Platform.runLater(() -> {

                                showError(
                                        "Booking Error",
                                        "Could not process the booking."
                                );
                            });
                        }

                    });


            // ---------------------------------------------------------
            // MAKE THREAD A DAEMON THREAD
            // ---------------------------------------------------------

            resultThread.setDaemon(true);


            // ---------------------------------------------------------
            // START THREAD
            // ---------------------------------------------------------

            resultThread.start();


        } catch (NumberFormatException e) {

            showError(
                    "Error",
                    "Please enter a valid Booking ID."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Error",
                    "Something went wrong while adding the booking."
            );
        }
    }


    // =========================================================
    // UPDATE BOOKING
    // =========================================================

    @FXML
    private void updateBooking() {

        Booking selectedBooking =
                bookingTable
                        .getSelectionModel()
                        .getSelectedItem();


        // ---------------------------------------------------------
        // CHECK SELECTION
        // ---------------------------------------------------------

        if (selectedBooking == null) {

            showWarning(
                    "Warning",
                    "Please select a booking first."
            );

            return;
        }


        // ---------------------------------------------------------
        // NEW CHECK-IN DATE
        // ---------------------------------------------------------

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


        // ---------------------------------------------------------
        // NEW CHECK-OUT DATE
        // ---------------------------------------------------------

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

            // ---------------------------------------------------------
            // CONVERT DATES
            // ---------------------------------------------------------

            LocalDate newCheckIn =
                    LocalDate.parse(
                            checkInResult.get()
                    );

            LocalDate newCheckOut =
                    LocalDate.parse(
                            checkOutResult.get()
                    );


            // ---------------------------------------------------------
            // VALIDATE DATES
            // ---------------------------------------------------------

            if (!newCheckOut.isAfter(newCheckIn)) {

                showError(
                        "Error",
                        "Check-out date must be after check-in date."
                );

                return;
            }


            // ---------------------------------------------------------
            // UPDATE BOOKING
            // ---------------------------------------------------------

            selectedBooking.setCheckIn(
                    newCheckIn
            );

            selectedBooking.setCheckOut(
                    newCheckOut
            );


            // ---------------------------------------------------------
            // REFRESH TABLE
            // ---------------------------------------------------------

            bookingTable.refresh();


            // ---------------------------------------------------------
            // SUCCESS MESSAGE
            // ---------------------------------------------------------

            showMessage(
                    "Success",
                    "Booking updated successfully!"
            );


        } catch (Exception e) {

            showError(
                    "Error",
                    "Please enter dates in YYYY-MM-DD format."
            );
        }
    }


    // =========================================================
    // DELETE BOOKING
    // =========================================================

    @FXML
    private void deleteBooking() {

        Booking selectedBooking =
                bookingTable
                        .getSelectionModel()
                        .getSelectedItem();


        // ---------------------------------------------------------
        // CHECK SELECTION
        // ---------------------------------------------------------

        if (selectedBooking == null) {

            showWarning(
                    "Warning",
                    "Please select a booking first."
            );

            return;
        }


        // ---------------------------------------------------------
        // CONFIRMATION ALERT
        // ---------------------------------------------------------

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


        // ---------------------------------------------------------
        // DELETE
        // ---------------------------------------------------------

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


    // =========================================================
    // BACK TO MAIN
    // =========================================================

    @FXML
    private void backToMain() {

        Stage stage =
                (Stage) bookingTable
                        .getScene()
                        .getWindow();

        stage.close();
    }


    // =========================================================
    // COMMON INFORMATION MESSAGE
    // =========================================================

    private void showMessage(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================================================
    // SHUTDOWN BOOKING SERVICE
    // =========================================================

    /*
     * Stops the ExecutorService when the controller
     * is no longer needed.
     */

    public void shutdownBookingService() {

        bookingService.shutdown();
    }
}