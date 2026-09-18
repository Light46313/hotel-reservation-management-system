package hotel.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Shared booking data for the whole application
public class BookingData {

    // One shared list of bookings
    public static final ObservableList<Booking> bookingList =
            FXCollections.observableArrayList();

    // Private constructor
    // This class is not meant to create objects
    private BookingData() {
    }
}