package hotel.controller;

import hotel.model.Customer;
import hotel.model.CustomerData;
import hotel.model.CustomerStorage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class CustomerController {

    // =========================
    // TABLE AND COLUMNS
    // =========================

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;


    // =========================
    // PART 6 - CUSTOMER IMAGE
    // =========================

    @FXML
    private ImageView customerImageView;


    // =========================
    // CUSTOMER LIST
    // =========================

    private ObservableList<Customer> customerList =
            CustomerData.customerList;


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        // Connect columns with Customer.java

        customerIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("customerId")
        );

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("address")
        );


        // Connect list with TableView

        customerTable.setItems(customerList);


        // =========================
        // LOAD SAVED CUSTOMERS
        // =========================

        // Clear the shared list first
        customerList.clear();

        // Load customers from customers.dat
        ObservableList<Customer> savedCustomers =
                CustomerStorage.loadCustomers();

        customerList.addAll(savedCustomers);


        // =========================
        // SAMPLE CUSTOMERS
        // =========================

        // Add sample customers only
        // if there are no saved customers.

        if (customerList.isEmpty()) {

            customerList.add(
                    new Customer(
                            1,
                            "Argho",
                            "01711111111",
                            "argho@gmail.com",
                            "Dhaka"
                    )
            );

            customerList.add(
                    new Customer(
                            2,
                            "Rahim",
                            "01822222222",
                            "rahim@gmail.com",
                            "Chittagong"
                    )
            );

            customerList.add(
                    new Customer(
                            3,
                            "Karim",
                            "01933333333",
                            "karim@gmail.com",
                            "Khulna"
                    )
            );

            // Save sample customers
            CustomerStorage.saveCustomers(customerList);
        }


        // =========================
        // PART 6 - CUSTOMER SELECTION
        // =========================

        // When the user selects a customer,
        // show that customer's saved photo.

        customerTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldCustomer, newCustomer) -> {

                            if (newCustomer != null
                                    && newCustomer.getPhotoPath() != null
                                    && !newCustomer.getPhotoPath().isEmpty()) {

                                Image image =
                                        new Image(
                                                newCustomer.getPhotoPath()
                                        );

                                customerImageView.setImage(image);

                            } else {

                                // No photo for this customer
                                customerImageView.setImage(null);
                            }
                        }
                );
    }


    // =========================
    // PART 6 - BROWSE IMAGE
    // =========================

    @FXML
    private void browseImage() {

        // Get the selected customer

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        // Make sure a customer is selected

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        // Create FileChooser

        FileChooser fileChooser =
                new FileChooser();


        // Set FileChooser title

        fileChooser.setTitle(
                "Select Customer Image"
        );


        // Allow image files only

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );


        // Get current window

        Stage stage =
                (Stage) customerImageView
                        .getScene()
                        .getWindow();


        // Open FileChooser

        File file =
                fileChooser.showOpenDialog(stage);


        // Check whether an image was selected

        if (file != null) {

            // Save the image path
            // inside the selected Customer

            selectedCustomer.setPhotoPath(
                    file.toURI().toString()
            );


            // =========================
            // SAVE CUSTOMER DATA
            // =========================

            // This makes the photo change permanent
            CustomerStorage.saveCustomers(customerList);


            // Create JavaFX Image

            Image image =
                    new Image(
                            file.toURI().toString()
                    );


            // Display image in ImageView

            customerImageView.setImage(image);
        }
    }


    // =========================
    // ADD CUSTOMER
    // =========================

    @FXML
    private void addCustomer() {

        // Customer ID

        TextInputDialog idDialog =
                new TextInputDialog();

        idDialog.setTitle("Add Customer");
        idDialog.setHeaderText("Enter Customer ID");
        idDialog.setContentText("Customer ID:");

        Optional<String> idResult =
                idDialog.showAndWait();

        if (idResult.isEmpty()) {
            return;
        }


        // Name

        TextInputDialog nameDialog =
                new TextInputDialog();

        nameDialog.setTitle("Add Customer");
        nameDialog.setHeaderText("Enter Customer Name");
        nameDialog.setContentText("Name:");

        Optional<String> nameResult =
                nameDialog.showAndWait();

        if (nameResult.isEmpty()) {
            return;
        }


        // Phone

        TextInputDialog phoneDialog =
                new TextInputDialog();

        phoneDialog.setTitle("Add Customer");
        phoneDialog.setHeaderText("Enter Phone Number");
        phoneDialog.setContentText("Phone:");

        Optional<String> phoneResult =
                phoneDialog.showAndWait();

        if (phoneResult.isEmpty()) {
            return;
        }


        // Email

        TextInputDialog emailDialog =
                new TextInputDialog();

        emailDialog.setTitle("Add Customer");
        emailDialog.setHeaderText("Enter Email");
        emailDialog.setContentText("Email:");

        Optional<String> emailResult =
                emailDialog.showAndWait();

        if (emailResult.isEmpty()) {
            return;
        }


        // Address

        TextInputDialog addressDialog =
                new TextInputDialog();

        addressDialog.setTitle("Add Customer");
        addressDialog.setHeaderText("Enter Address");
        addressDialog.setContentText("Address:");

        Optional<String> addressResult =
                addressDialog.showAndWait();

        if (addressResult.isEmpty()) {
            return;
        }


        try {

            int customerId =
                    Integer.parseInt(idResult.get());

            String name =
                    nameResult.get();

            String phone =
                    phoneResult.get();

            String email =
                    emailResult.get();

            String address =
                    addressResult.get();


            Customer customer =
                    new Customer(
                            customerId,
                            name,
                            phone,
                            email,
                            address
                    );


            customerList.add(customer);


            // Save new customer permanently
            CustomerStorage.saveCustomers(customerList);


            showMessage(
                    "Success",
                    "Customer added successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Customer ID must be a valid number."
            );
        }
    }


    // =========================
    // UPDATE CUSTOMER
    // =========================

    @FXML
    private void updateCustomer() {

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        // New Name

        TextInputDialog nameDialog =
                new TextInputDialog(
                        selectedCustomer.getName()
                );

        nameDialog.setTitle("Update Customer");
        nameDialog.setHeaderText("Update Customer Name");
        nameDialog.setContentText("Name:");

        Optional<String> nameResult =
                nameDialog.showAndWait();

        if (nameResult.isEmpty()) {
            return;
        }


        // New Phone

        TextInputDialog phoneDialog =
                new TextInputDialog(
                        selectedCustomer.getPhone()
                );

        phoneDialog.setTitle("Update Customer");
        phoneDialog.setHeaderText("Update Phone Number");
        phoneDialog.setContentText("Phone:");

        Optional<String> phoneResult =
                phoneDialog.showAndWait();

        if (phoneResult.isEmpty()) {
            return;
        }


        // New Email

        TextInputDialog emailDialog =
                new TextInputDialog(
                        selectedCustomer.getEmail()
                );

        emailDialog.setTitle("Update Customer");
        emailDialog.setHeaderText("Update Email");
        emailDialog.setContentText("Email:");

        Optional<String> emailResult =
                emailDialog.showAndWait();

        if (emailResult.isEmpty()) {
            return;
        }


        // New Address

        TextInputDialog addressDialog =
                new TextInputDialog(
                        selectedCustomer.getAddress()
                );

        addressDialog.setTitle("Update Customer");
        addressDialog.setHeaderText("Update Address");
        addressDialog.setContentText("Address:");

        Optional<String> addressResult =
                addressDialog.showAndWait();

        if (addressResult.isEmpty()) {
            return;
        }


        // Update the selected customer

        selectedCustomer.setName(
                nameResult.get()
        );

        selectedCustomer.setPhone(
                phoneResult.get()
        );

        selectedCustomer.setEmail(
                emailResult.get()
        );

        selectedCustomer.setAddress(
                addressResult.get()
        );


        // Refresh table

        customerTable.refresh();


        // Save updated customer permanently
        CustomerStorage.saveCustomers(customerList);


        showMessage(
                "Success",
                "Customer updated successfully!"
        );
    }


    // =========================
    // DELETE CUSTOMER
    // =========================

    @FXML
    private void deleteCustomer() {

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle("Delete Customer");

        confirmation.setHeaderText(
                "Delete Customer " +
                        selectedCustomer.getName() + "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this customer?"
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            customerList.remove(
                    selectedCustomer
            );


            // Clear ImageView after deleting
            // the selected customer

            customerImageView.setImage(null);


            // Save deletion permanently
            CustomerStorage.saveCustomers(customerList);


            showMessage(
                    "Success",
                    "Customer deleted successfully!"
            );
        }
    }


    // =========================
    // BOOK SELECTED CUSTOMER
    // =========================

    @FXML
    private void bookSelectedCustomer() {

        // Get the customer selected in the TableView

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        // Check whether a customer was selected

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        try {

            // Load BookingView.fxml

            FXMLLoader loader = new FXMLLoader(
                    CustomerController.class.getResource(
                            "/view/BookingView.fxml"
                    )
            );


            Scene scene =
                    new Scene(loader.load());


            // Get the BookingController

            BookingController controller =
                    loader.getController();


            // Pass the selected Customer
            // from CustomerController
            // to BookingController

            controller.setSelectedCustomer(
                    selectedCustomer
            );


            // Open Booking Management

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
    // BACK TO MAIN
    // =========================

    @FXML
    private void backToMain() {

        Stage stage =
                (Stage) customerTable
                        .getScene()
                        .getWindow();

        stage.close();
    }


    // =========================
    // SHOW MESSAGE
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