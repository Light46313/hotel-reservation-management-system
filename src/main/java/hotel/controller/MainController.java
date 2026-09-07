package hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainController {

    // This method will be connected to the Room Management button
    @FXML
    private void openRoomManagement() {
        showMessage("Room Management", "Room Management section will open here.");
    }

    // This method will be connected to the Customer Management button
    @FXML
    private void openCustomerManagement() {
        showMessage("Customer Management", "Customer Management section will open here.");
    }

    // This method will be connected to the Booking button
    @FXML
    private void openBooking() {
        showMessage("Booking", "Booking section will open here.");
    }

    // This method will be connected to the Booking History button
    @FXML
    private void openBookingHistory() {
        showMessage("Booking History", "Booking History section will open here.");
    }

    // This method will be connected to the Exit button
    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    // Common method for showing information
    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}