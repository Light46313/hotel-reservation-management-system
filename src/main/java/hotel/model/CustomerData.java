package hotel.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerData {

    // Shared customer list for the whole application
    public static final ObservableList<Customer> customerList =
            FXCollections.observableArrayList();

    // Prevent creating objects of this class
    private CustomerData() {
    }
}