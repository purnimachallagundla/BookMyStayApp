import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

// Reservation Entity
class Reservation {
    private static int counter = 1;

    private int reservationId;
    private String roomType;

    public Reservation(String roomType) {
        this.reservationId = counter++; // unique ID
        this.roomType = roomType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory (Centralized State)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current > 0) {
            inventory.put(type, current - 1);
        }
    }
}

// Allocation + Confirmation Service
class ReservationService {

    public Reservation confirmBooking(RoomInventory inventory, String roomType) {

        System.out.println("\nProcessing booking for: " + roomType);

        int available = inventory.getAvailability(roomType);

        if (available > 0) {

            // Step 1: Allocate room (reduce count)
            inventory.reduceAvailability(roomType);

            // Step 2: Create reservation
            Reservation reservation = new Reservation(roomType);

            // Step 3: Confirmation
            System.out.println("Booking CONFIRMED!");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Room Type: " + reservation.getRoomType());

            return reservation;

        } else {
            System.out.println("Booking FAILED! No rooms available.");
            return null;
        }
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Suite", 1);

        // Step 2: Reservation Service
        ReservationService service = new ReservationService();

        // Step 3: Booking Requests
        service.confirmBooking(inventory, "Single"); // success
        service.confirmBooking(inventory, "Single"); // fail

        service.confirmBooking(inventory, "Suite");  // success
    }