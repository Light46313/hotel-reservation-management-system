package hotel.controller;

import hotel.database.CustomerDAO;
import hotel.model.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

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
    // CUSTOMER IMAGE
    // =========================

    @FXML
    private ImageView customerImageView;


    // =========================
    // SPECIAL REQUEST
    // =========================

    @FXML
    private TextArea specialRequestTextArea;


    // =========================
    // CUSTOMER LIST
    // =========================

    private final ObservableList<Customer> customerList =
            FXCollections.observableArrayList();


    // =========================
    // CUSTOMER DAO
    // =========================

    private final CustomerDAO customerDAO =
            new CustomerDAO();


    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(
            URL location,
            ResourceBundle resources
    ) {

        // =========================
        // CONNECT TABLE COLUMNS
        // =========================

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


        // =========================
        // CONNECT LIST WITH TABLE
        // =========================

        customerTable.setItems(customerList);


        // =========================
        // LOAD CUSTOMERS FROM SQLITE
        // =========================

        loadCustomersFromDatabase();


        // =========================
        // AUTOMATICALLY SELECT
        // FIRST CUSTOMER
        // =========================

        if (!customerList.isEmpty()) {

            customerTable
                    .getSelectionModel()
                    .selectFirst();

            loadCustomerImage(
                    customerList.get(0)
            );
        }


        // =========================
        // CUSTOMER SELECTION
        // =========================

        customerTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         oldCustomer,
                         newCustomer) -> {

                            if (newCustomer != null) {

                                loadCustomerImage(
                                        newCustomer
                                );
                            }
                        }
                );
    }


    // =========================
    // LOAD CUSTOMERS FROM SQLITE
    // =========================

    private void loadCustomersFromDatabase() {

        customerList.clear();

        List<Customer> customers =
                customerDAO.getAllCustomers();

        customerList.addAll(customers);
    }


    // =========================
    // LOAD CUSTOMER IMAGE
    // =========================

    private void loadCustomerImage(
            Customer customer
    ) {

        if (customer == null) {

            customerImageView.setImage(null);

            return;
        }


        if (customer.getPhotoPath() != null
                && !customer.getPhotoPath().isEmpty()) {

            try {

                Image image =
                        new Image(
                                customer.getPhotoPath()
                        );

                customerImageView.setImage(image);

            } catch (Exception e) {

                customerImageView.setImage(null);
            }

        } else {

            customerImageView.setImage(null);
        }
    }


    // =========================
    // BROWSE IMAGE
    // =========================

    @FXML
    private void browseImage() {

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        // =========================
        // FILE CHOOSER
        // =========================

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Select Customer Image"
        );


        // =========================
        // IMAGE FILTER
        // =========================

        fileChooser
                .getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Image Files",
                                "*.png",
                                "*.jpg",
                                "*.jpeg"
                        )
                );


        // =========================
        // GET CURRENT WINDOW
        // =========================

        Stage stage =
                (Stage) customerImageView
                        .getScene()
                        .getWindow();


        // =========================
        // OPEN FILE CHOOSER
        // =========================

        File file =
                fileChooser.showOpenDialog(stage);


        // =========================
        // SAVE IMAGE
        // =========================

        if (file != null) {

            String photoPath =
                    file.toURI().toString();

            selectedCustomer.setPhotoPath(
                    photoPath
            );


            // =========================
            // SAVE PHOTO PATH TO SQLITE
            // =========================

            customerDAO.updateCustomer(
                    selectedCustomer
            );


            // =========================
            // DISPLAY IMAGE
            // =========================

            try {

                Image image =
                        new Image(photoPath);

                customerImageView.setImage(image);

            } catch (Exception e) {

                customerImageView.setImage(null);
            }


            showMessage(
                    "Success",
                    "Customer image updated successfully!"
            );
        }
    }


    // =========================
    // ADD CUSTOMER
    // =========================

    @FXML
    private void addCustomer() {

        // =========================
        // CUSTOMER ID
        // =========================

        TextInputDialog idDialog =
                new TextInputDialog();

        idDialog.setTitle(
                "Add Customer"
        );

        idDialog.setHeaderText(
                "Enter Customer ID"
        );

        idDialog.setContentText(
                "Customer ID:"
        );


        Optional<String> idResult =
                idDialog.showAndWait();

        if (idResult.isEmpty()) {
            return;
        }


        // =========================
        // NAME
        // =========================

        TextInputDialog nameDialog =
                new TextInputDialog();

        nameDialog.setTitle(
                "Add Customer"
        );

        nameDialog.setHeaderText(
                "Enter Customer Name"
        );

        nameDialog.setContentText(
                "Name:"
        );


        Optional<String> nameResult =
                nameDialog.showAndWait();

        if (nameResult.isEmpty()) {
            return;
        }


        // =========================
        // PHONE
        // =========================

        TextInputDialog phoneDialog =
                new TextInputDialog();

        phoneDialog.setTitle(
                "Add Customer"
        );

        phoneDialog.setHeaderText(
                "Enter Phone Number"
        );

        phoneDialog.setContentText(
                "Phone:"
        );


        Optional<String> phoneResult =
                phoneDialog.showAndWait();

        if (phoneResult.isEmpty()) {
            return;
        }


        // =========================
        // EMAIL
        // =========================

        TextInputDialog emailDialog =
                new TextInputDialog();

        emailDialog.setTitle(
                "Add Customer"
        );

        emailDialog.setHeaderText(
                "Enter Email"
        );

        emailDialog.setContentText(
                "Email:"
        );


        Optional<String> emailResult =
                emailDialog.showAndWait();

        if (emailResult.isEmpty()) {
            return;
        }


        // =========================
        // ADDRESS
        // =========================

        TextInputDialog addressDialog =
                new TextInputDialog();

        addressDialog.setTitle(
                "Add Customer"
        );

        addressDialog.setHeaderText(
                "Enter Address"
        );

        addressDialog.setContentText(
                "Address:"
        );


        Optional<String> addressResult =
                addressDialog.showAndWait();

        if (addressResult.isEmpty()) {
            return;
        }


        try {

            // =========================
            // READ VALUES
            // =========================

            int customerId =
                    Integer.parseInt(
                            idResult.get().trim()
                    );

            String name =
                    nameResult.get().trim();

            String phone =
                    phoneResult.get().trim();

            String email =
                    emailResult.get().trim();

            String address =
                    addressResult.get().trim();


            // =========================
            // VALIDATION
            // =========================

            if (name.isEmpty()) {

                showMessage(
                        "Error",
                        "Customer name cannot be empty."
                );

                return;
            }

            if (customerId <= 0) {

                showMessage(
                        "Error",
                        "Customer ID must be greater than 0."
                );

                return;
            }


            // =========================
            // CHECK DUPLICATE ID
            // =========================

            Customer existingCustomer =
                    customerDAO.getCustomerById(
                            customerId
                    );

            if (existingCustomer != null) {

                showMessage(
                        "Error",
                        "Customer ID already exists."
                );

                return;
            }


            // =========================
            // CREATE CUSTOMER
            // =========================

            Customer customer =
                    new Customer(
                            customerId,
                            name,
                            phone,
                            email,
                            address
                    );


            // =========================
            // SAVE TO SQLITE
            // =========================

            customerDAO.addCustomer(
                    customer
            );


            // =========================
            // RELOAD DATABASE DATA
            // =========================

            loadCustomersFromDatabase();


            // =========================
            // SELECT NEW CUSTOMER
            // =========================

            for (Customer c : customerList) {

                if (c.getCustomerId()
                        == customerId) {

                    customerTable
                            .getSelectionModel()
                            .select(c);

                    customerTable
                            .scrollTo(c);

                    break;
                }
            }


            showMessage(
                    "Success",
                    "Customer added successfully!"
            );


        } catch (NumberFormatException e) {

            showMessage(
                    "Error",
                    "Customer ID must be a valid number."
            );

        } catch (RuntimeException e) {

            showMessage(
                    "Database Error",
                    e.getMessage()
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


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        // =========================
        // NAME
        // =========================

        TextInputDialog nameDialog =
                new TextInputDialog(
                        selectedCustomer.getName()
                );

        nameDialog.setTitle(
                "Update Customer"
        );

        nameDialog.setHeaderText(
                "Update Customer Name"
        );

        nameDialog.setContentText(
                "Name:"
        );


        Optional<String> nameResult =
                nameDialog.showAndWait();

        if (nameResult.isEmpty()) {
            return;
        }


        // =========================
        // PHONE
        // =========================

        TextInputDialog phoneDialog =
                new TextInputDialog(
                        selectedCustomer.getPhone()
                );

        phoneDialog.setTitle(
                "Update Customer"
        );

        phoneDialog.setHeaderText(
                "Update Phone Number"
        );

        phoneDialog.setContentText(
                "Phone:"
        );


        Optional<String> phoneResult =
                phoneDialog.showAndWait();

        if (phoneResult.isEmpty()) {
            return;
        }


        // =========================
        // EMAIL
        // =========================

        TextInputDialog emailDialog =
                new TextInputDialog(
                        selectedCustomer.getEmail()
                );

        emailDialog.setTitle(
                "Update Customer"
        );

        emailDialog.setHeaderText(
                "Update Email"
        );

        emailDialog.setContentText(
                "Email:"
        );


        Optional<String> emailResult =
                emailDialog.showAndWait();

        if (emailResult.isEmpty()) {
            return;
        }


        // =========================
        // ADDRESS
        // =========================

        TextInputDialog addressDialog =
                new TextInputDialog(
                        selectedCustomer.getAddress()
                );

        addressDialog.setTitle(
                "Update Customer"
        );

        addressDialog.setHeaderText(
                "Update Address"
        );

        addressDialog.setContentText(
                "Address:"
        );


        Optional<String> addressResult =
                addressDialog.showAndWait();

        if (addressResult.isEmpty()) {
            return;
        }


        // =========================
        // READ VALUES
        // =========================

        String newName =
                nameResult.get().trim();

        String newPhone =
                phoneResult.get().trim();

        String newEmail =
                emailResult.get().trim();

        String newAddress =
                addressResult.get().trim();


        // =========================
        // VALIDATION
        // =========================

        if (newName.isEmpty()) {

            showMessage(
                    "Error",
                    "Customer name cannot be empty."
            );

            return;
        }


        // =========================
        // UPDATE OBJECT
        // =========================

        selectedCustomer.setName(
                newName
        );

        selectedCustomer.setPhone(
                newPhone
        );

        selectedCustomer.setEmail(
                newEmail
        );

        selectedCustomer.setAddress(
                newAddress
        );


        try {

            // =========================
            // UPDATE SQLITE
            // =========================

            customerDAO.updateCustomer(
                    selectedCustomer
            );


            // =========================
            // RELOAD DATA
            // =========================

            loadCustomersFromDatabase();


            // =========================
            // RESELECT CUSTOMER
            // =========================

            for (Customer customer :
                    customerList) {

                if (customer.getCustomerId()
                        == selectedCustomer.getCustomerId()) {

                    customerTable
                            .getSelectionModel()
                            .select(customer);

                    break;
                }
            }


            showMessage(
                    "Success",
                    "Customer updated successfully!"
            );

        } catch (RuntimeException e) {

            showMessage(
                    "Database Error",
                    e.getMessage()
            );
        }
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


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        // =========================
        // CONFIRMATION
        // =========================

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Customer"
        );

        confirmation.setHeaderText(
                "Delete Customer "
                        + selectedCustomer.getName()
                        + "?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete this customer?"
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        // =========================
        // DELETE
        // =========================

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            try {

                customerDAO.deleteCustomer(
                        selectedCustomer.getCustomerId()
                );


                // =========================
                // RELOAD DATABASE
                // =========================

                loadCustomersFromDatabase();


                // =========================
                // CLEAR IMAGE
                // =========================

                customerImageView.setImage(null);


                showMessage(
                        "Success",
                        "Customer deleted successfully!"
                );

            } catch (RuntimeException e) {

                showMessage(
                        "Database Error",
                        e.getMessage()
                );
            }
        }
    }


    // =========================
    // CLEAR SPECIAL REQUEST
    // =========================

    @FXML
    private void clearSpecialRequest() {

        if (specialRequestTextArea != null) {

            specialRequestTextArea.clear();
        }
    }


    // =========================
    // BOOK SELECTED CUSTOMER
    // =========================

    @FXML
    private void bookSelectedCustomer() {

        Customer selectedCustomer =
                customerTable
                        .getSelectionModel()
                        .getSelectedItem();


        // =========================
        // CHECK SELECTION
        // =========================

        if (selectedCustomer == null) {

            showMessage(
                    "Warning",
                    "Please select a customer first."
            );

            return;
        }


        try {

            // =========================
            // LOAD BOOKING VIEW
            // =========================

            FXMLLoader loader =
                    new FXMLLoader(
                            CustomerController.class
                                    .getResource(
                                            "/view/BookingView.fxml"
                                    )
                    );


            Scene scene =
                    new Scene(
                            loader.load()
                    );


            // =========================
            // GET BOOKING CONTROLLER
            // =========================

            BookingController controller =
                    loader.getController();


            // =========================
            // PASS CUSTOMER
            // =========================

            controller.setSelectedCustomer(
                    selectedCustomer
            );


            // =========================
            // OPEN BOOKING WINDOW
            // =========================

            Stage stage =
                    new Stage();

            stage.setTitle(
                    "Booking Management"
            );

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