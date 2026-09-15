package hotel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    // Label used for testing event handling
    @FXML
    private Label eventStatusLabel;


    // =========================
    // ROOM MANAGEMENT
    // =========================

    @FXML
    private void openRoomManagement() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource("/view/RoomView.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setTitle("Room Management");
            stage.setScene(scene);

            stage.setWidth(700);
            stage.setHeight(500);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Error",
                    "Could not open Room Management."
            );
        }
    }


    // =========================
    // CUSTOMER MANAGEMENT
    // =========================

    @FXML
    private void openCustomerManagement() {

        showMessage(
                "Customer Management",
                "Customer Management section will open here."
        );
    }


    // =========================
    // BOOKING
    // =========================

    @FXML
    private void openBooking() {

        showMessage(
                "Booking",
                "Booking section will open here."
        );
    }


    // =========================
    // BOOKING HISTORY
    // =========================

    @FXML
    private void openBookingHistory() {

        showMessage(
                "Booking History",
                "Booking History section will open here."
        );
    }


    // =========================
    // EXIT
    // =========================

    @FXML
    private void exitApplication() {

        System.exit(0);
    }


    // =========================
    // TEST EVENT
    // =========================

    @FXML
    private void handleTestEvent() {

        eventStatusLabel.setText(
                "Button Clicked!"
        );
    }


    // =========================
    // COMMON MESSAGE
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