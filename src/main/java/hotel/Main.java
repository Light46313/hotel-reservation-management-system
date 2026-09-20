package hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Start with Login Page
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/view/LoginView.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Hotel Reservation and Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}