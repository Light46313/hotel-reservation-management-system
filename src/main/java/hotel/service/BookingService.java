package hotel.service;

import hotel.model.Booking;
import hotel.model.Customer;
import hotel.model.Room;

import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BookingService {

    // =========================================
    // EXECUTOR SERVICE
    // =========================================

    /*
     * Fixed thread pool with 3 worker threads.
     *
     * Multiple booking requests can be processed
     * without creating unlimited threads.
     */
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(3);


    // =========================================
    // CREATE BOOKING ASYNC
    // =========================================

    /*
     * This method submits the booking task
     * to the ExecutorService.
     *
     * Future<Booking> represents the result
     * that will be available after the worker
     * thread finishes the task.
     */
    public Future<Booking> createBookingAsync(
            int bookingId,
            Customer customer,
            Room room,
            LocalDate checkIn,
            LocalDate checkOut) {

        Callable<Booking> task = () -> {

            return processBooking(
                    bookingId,
                    customer,
                    room,
                    checkIn,
                    checkOut
            );
        };


        return executorService.submit(task);
    }


    // =========================================
    // PROCESS BOOKING
    // =========================================

    /*
     * This method performs the actual booking work.
     *
     * It runs inside a worker thread because
     * createBookingAsync() submits it to the
     * ExecutorService.
     */
    private Booking processBooking(
            int bookingId,
            Customer customer,
            Room room,
            LocalDate checkIn,
            LocalDate checkOut) {


        // -----------------------------------------
        // CHECK INPUT
        // -----------------------------------------

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }


        if (room == null) {
            throw new IllegalArgumentException(
                    "Room cannot be null."
            );
        }


        if (checkIn == null) {
            throw new IllegalArgumentException(
                    "Check-in date cannot be null."
            );
        }


        if (checkOut == null) {
            throw new IllegalArgumentException(
                    "Check-out date cannot be null."
            );
        }


        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date."
            );
        }


        // -----------------------------------------
        // SIMULATE PROCESSING TIME
        // -----------------------------------------

        /*
         * This is only to demonstrate that the
         * booking task is being processed by a
         * separate worker thread.
         */
        try {

            Thread.sleep(1000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Booking process was interrupted.",
                    e
            );
        }


        // -----------------------------------------
        // CHECK ROOM AVAILABILITY
        // -----------------------------------------

        synchronized (room) {

            if (!room.isAvailable()) {

                throw new IllegalStateException(
                        "Room "
                                + room.getRoomNumber()
                                + " is not available."
                );
            }


            // -----------------------------------------
            // CREATE BOOKING
            // -----------------------------------------

            Booking booking =
                    new Booking(
                            bookingId,
                            customer,
                            room,
                            checkIn,
                            checkOut
                    );


            // -----------------------------------------
            // MARK ROOM UNAVAILABLE
            // -----------------------------------------

            room.setAvailable(false);


            // -----------------------------------------
            // RETURN BOOKING
            // -----------------------------------------

            return booking;
        }
    }


    // =========================================
    // SHUTDOWN
    // =========================================

    /*
     * Stops the ExecutorService when the application
     * no longer needs to process booking tasks.
     */
    public void shutdown() {

        executorService.shutdown();
    }
}