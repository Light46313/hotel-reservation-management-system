package hotel.controller;

import hotel.model.Weather;
import hotel.service.WeatherService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.concurrent.Future;

public class WeatherController {

    // =========================================================
    // FXML CONTROLS
    // =========================================================

    @FXML
    private TextField cityField;

    @FXML
    private Button refreshWeatherButton;

    @FXML
    private Label cityLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label conditionLabel;

    @FXML
    private Label statusLabel;


    // =========================================================
    // WEATHER SERVICE
    // =========================================================

    private final WeatherService weatherService =
            new WeatherService();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        // Default city
        cityField.setText("Khulna");

        // Initial status message
        statusLabel.setText(
                "Enter a city and click Refresh Weather."
        );
    }


    // =========================================================
    // REFRESH WEATHER
    // =========================================================

    @FXML
    private void refreshWeather() {

        // Get city name
        String city =
                cityField.getText().trim();


        // =====================================================
        // INPUT VALIDATION
        // =====================================================

        if (city.isEmpty()) {

            showError(
                    "Input Error",
                    "Please enter a city name."
            );

            return;
        }


        // =====================================================
        // DISABLE BUTTON WHILE REQUEST IS RUNNING
        // =====================================================

        refreshWeatherButton.setDisable(true);

        statusLabel.setText(
                "Fetching weather data..."
        );


        // =====================================================
        // START ASYNCHRONOUS WEATHER REQUEST
        // =====================================================

        Future<Weather> future =
                weatherService.getWeatherAsync(city);


        // =====================================================
        // WAIT FOR RESULT IN BACKGROUND THREAD
        // =====================================================

        Thread resultThread = new Thread(() -> {

            try {

                // Wait for WeatherService result
                Weather weather =
                        future.get();


                // =================================================
                // UPDATE JAVAFX UI
                // =================================================

                Platform.runLater(() -> {

                    // City
                    cityLabel.setText(
                            weather.getCity()
                    );


                    // Temperature
                    temperatureLabel.setText(
                            String.format(
                                    "%.1f °C",
                                    weather.getTemperature()
                            )
                    );


                    // Humidity
                    humidityLabel.setText(
                            weather.getHumidity() + "%"
                    );


                    // Weather condition
                    conditionLabel.setText(
                            weather.getCondition()
                    );


                    // Status
                    statusLabel.setText(
                            "Weather data updated successfully."
                    );


                    // Enable button again
                    refreshWeatherButton.setDisable(false);
                });

            } catch (Exception e) {

                e.printStackTrace();


                // =================================================
                // HANDLE ERROR ON JAVAFX THREAD
                // =================================================

                Platform.runLater(() -> {

                    statusLabel.setText(
                            "Unable to load weather data."
                    );


                    refreshWeatherButton.setDisable(false);


                    showError(
                            "Weather Error",
                            "Could not retrieve weather information."
                    );
                });
            }

        });


        // Make the result thread a daemon thread
        resultThread.setDaemon(true);


        // Start result thread
        resultThread.start();
    }


    // =========================================================
    // ERROR ALERT
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
    // BACK TO MAIN DASHBOARD
    // =========================================================

    @FXML
    private void backToMain() {

        // Stop WeatherService thread pool
        weatherService.shutdown();


        // Get current Weather window
        Stage stage =
                (Stage) cityField
                        .getScene()
                        .getWindow();


        // Close Weather window
        stage.close();
    }
}