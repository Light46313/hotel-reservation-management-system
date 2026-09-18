package hotel.controller;

import hotel.model.Booking;
import hotel.model.BookingData;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookingHistoryController {

    @FXML
    private TableView<Booking> bookingHistoryTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> customerColumn;

    @FXML
    private TableColumn<Booking, String> roomColumn;

    @FXML
    private TableColumn<Booking, String> checkInColumn;

    @FXML
    private TableColumn<Booking, String> checkOutColumn;

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

        // Connect Check-In column
        checkInColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkIn")
        );

        // Connect Check-Out column
        checkOutColumn.setCellValueFactory(
                new PropertyValueFactory<>("checkOut")
        );

        // Use the SAME shared booking list
        // used by BookingController
        bookingHistoryTable.setItems(
                BookingData.bookingList
        );
    }

    @FXML
    private void backToMain() {

        Stage stage =
                (Stage) bookingHistoryTable
                        .getScene()
                        .getWindow();

        stage.close();
    }
}