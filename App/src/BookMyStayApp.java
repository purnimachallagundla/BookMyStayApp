
import java.io.*;
import java.util.*;

// Reservation Model (Serializable for file storage)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1;

    private int reservationId;
    private String roomType;

    public Reservation(String roomType) {
        this.reservationId = counter++;
        this.roomType = roomType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + ", Room: " + roomType;
    }
}

// Persistence Manager
class PersistenceManager {

    private static final String FILE_NAME = "reservations.dat";

    // Save data to file
    public static void save(List<Reservation> reservations) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(reservations);
            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data from file
    public static List<Reservation> load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (List<Reservation>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

// Booking System
class BookingSystem {

    private List<Reservation> reservations;

    public BookingSystem() {
        // Load previous data (Recovery)
        reservations = PersistenceManager.load();
        System.out.println("System recovered. Loaded bookings: " + reservations.size());
    }

    public void bookRoom(String roomType) {
        Reservation r = new Reservation(roomType);
        reservations.add(r);
        System.out.println("Booking CONFIRMED! " + r);
    }

    public void showAll() {
        System.out.println("\n--- All Reservations ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void shutdown() {
        // Save data before exit
        PersistenceManager.save(reservations);
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulate bookings
        system.bookRoom("Single");
        system.bookRoom("Suite");

        system.showAll();

        // Save before exit
        system.shutdown();
    }