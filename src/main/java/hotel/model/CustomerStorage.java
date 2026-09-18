package hotel.model;

import java.io.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerStorage {

    private static final String FILE_NAME = "customers.dat";


    // =========================
    // SAVE CUSTOMERS
    // =========================

    public static void saveCustomers(
            ObservableList<Customer> customerList) {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {

            // Convert ObservableList to ArrayList
            ArrayList<Customer> customers =
                    new ArrayList<>(customerList);

            output.writeObject(customers);

            System.out.println("Customer data saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error while saving customer data."
            );

            e.printStackTrace();
        }
    }


    // =========================
    // LOAD CUSTOMERS
    // =========================

    public static ObservableList<Customer> loadCustomers() {

        File file = new File(FILE_NAME);

        // If file does not exist
        if (!file.exists()) {

            System.out.println(
                    "No customer data file found."
            );

            return FXCollections.observableArrayList();
        }


        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {

            ArrayList<Customer> customers =
                    (ArrayList<Customer>) input.readObject();

            System.out.println(
                    "Customer data loaded successfully."
            );

            return FXCollections.observableArrayList(
                    customers
            );

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Error while loading customer data."
            );

            e.printStackTrace();

            return FXCollections.observableArrayList();
        }
    }
}