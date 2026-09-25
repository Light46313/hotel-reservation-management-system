package hotel.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainController {

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

        // File -> New opens
        // the Booking Management window.

        openBooking();
    }


    // =========================
    // MENU BAR - OPEN
    // =========================

    @FXML
    private void openAction() {

        // For now, Open shows a message.
        // This can be connected to a file/database
        // later if required.

        showMessage(
                "Open",
                "Open option selected."
        );
    }


    // =========================
    // REFRESH DATA
    // =========================

    @FXML
    private void refreshData() {

        /*
         * The dashboard does not currently contain
         * data that needs to be reloaded directly.
         *
         * Room, customer and booking data are handled
         * by their respective management screens.
         */

        showMessage(
                "Refresh Data",
                "Dashboard data has been refreshed successfully."
        );
    }


    // =========================
    // LOGOUT
    // =========================

    @FXML
    private void logout(ActionEvent event) {

        try {

            // Get the current Main Dashboard window
            Stage currentStage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            // Load Login page
            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource(
                            "/view/LoginView.fxml"
                    )
            );

            Scene loginScene =
                    new Scene(loader.load());

            // Close current dashboard
            currentStage.close();

            // Create Login window
            Stage loginStage = new Stage();

            loginStage.setTitle(
                    "Hotel Reservation and Management System"
            );

            loginStage.setScene(loginScene);

            loginStage.setResizable(false);

            loginStage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Logout Error",
                    "Could not return to the Login page."
            );
        }
    }


    // =========================
    // EXIT APPLICATION
    // =========================

    @FXML
    private void exitApplication() {

        System.exit(0);
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