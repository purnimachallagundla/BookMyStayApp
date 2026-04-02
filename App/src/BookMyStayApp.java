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

    // UPDATE after booking
    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current > 0) {
            inventory.put(type, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Service (First-Come-First-Served)
class BookingService {

    public void bookRoom(RoomInventory inventory, String roomType) {

        System.out.println("\nRequest received for: " + roomType);

        int available = inventory.getAvailability(roomType);

        // FCFS Logic
        if (available > 0) {
            inventory.reduceAvailability(roomType);
            System.out.println("Booking CONFIRMED for " + roomType);
        } else {
            System.out.println("Booking FAILED for " + roomType + " (No rooms available)");
        }
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        // Step 1: Setup Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);

        // Step 2: Booking Service
        BookingService bookingService = new BookingService();

        // Step 3: Simulate requests (First-Come-First-Served)
        bookingService.bookRoom(inventory, "Single"); // success
        bookingService.bookRoom(inventory, "Single"); // success
        bookingService.bookRoom(inventory, "Single"); // fail (no rooms left)

        bookingService.bookRoom(inventory, "Double"); // success
        bookingService.bookRoom(inventory, "Double"); // fail

        // Step 4: Final inventory state
        inventory.displayInventory();
    }