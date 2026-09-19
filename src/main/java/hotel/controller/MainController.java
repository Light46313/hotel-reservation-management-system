package hotel.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {

    // =========================
    // EVENT STATUS LABEL
    // =========================

    @FXML
    private Label eventStatusLabel;


    // =========================
    // ROOM MANAGEMENT
    // =========================

    @FXML
    private void openRoomManagement() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource(
                            "/view/RoomView.fxml"
                    )
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

        try {

            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource(
                            "/view/CustomerView.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setTitle("Customer Management");
            stage.setScene(scene);

            stage.setWidth(700);
            stage.setHeight(500);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Error",
                    "Could not open Customer Management."
            );
        }
    }


    // =========================
    // BOOKING MANAGEMENT
    // =========================

    @FXML
    private void openBooking() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource(
                            "/view/BookingView.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setTitle("Booking Management");
            stage.setScene(scene);

            stage.setWidth(800);
            stage.setHeight(550);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Error",
                    "Could not open Booking Management."
            );
        }
    }


    // =========================
    // BOOKING HISTORY
    // =========================

    @FXML
    private void openBookingHistory() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource(
                            "/view/BookingHistoryView.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setTitle("Booking History");
            stage.setScene(scene);

            stage.setWidth(800);
            stage.setHeight(500);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Error",
                    "Could not open Booking History."
            );
        }
    }


    // =========================
    // MENU BAR - NEW
    // =========================

    @FXML
    private void newAction() {

        // File -> New will open
        // the Booking Management window.

        openBooking();
    }


    // =========================
    // MENU BAR - OPEN
    // =========================

    @FXML
    private void openAction() {

        // For now, Open shows a message.
        // We can give this menu item
        // another function later.

        showMessage(
                "Open",
                "Open option selected."
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

        if (eventStatusLabel != null) {

            eventStatusLabel.setText(
                    "Button Clicked!"
            );
        }
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