import java.util.*;

// Room Model
class Room {
    private String type;
    private double price;

    public Room(String type, double price) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Invalid room price");
        }
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

// Reservation
class Reservation {
    private static int counter = 1;

    private int reservationId;
    private Room room;

    public Reservation(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.reservationId = counter++;
        this.room = room;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Room getRoom() {
        return room;
    }
}

// Inventory
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current <= 0) {
            throw new IllegalStateException("No rooms available");
        }
        inventory.put(type, current - 1);
    }

    // Rollback method (IMPORTANT)
    public void increaseAvailability(String type) {
        int current = getAvailability(type);
        inventory.put(type, current + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// Booking History
class BookingHistory {
    private Map<Integer, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(int id) {
        return reservations.get(id);
    }

    public void removeReservation(int id) {
        reservations.remove(id);
    }

    public void showAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations.values()) {
            System.out.println("ID: " + r.getReservationId() +
                    ", Room: " + r.getRoom().getType());
        }
    }
}

// Reservation Service
class ReservationService {

    private BookingHistory history;

    public ReservationService(BookingHistory history) {
        this.history = history;
    }

    // Booking
    public Reservation bookRoom(RoomInventory inventory, Room room) {
        int available = inventory.getAvailability(room.getType());

        if (available > 0) {
            inventory.reduceAvailability(room.getType());

            Reservation r = new Reservation(room);
            history.addReservation(r);

            System.out.println("Booking CONFIRMED! ID: " + r.getReservationId());
            return r;
        } else {
            System.out.println("Booking FAILED!");
            return null;
        }
    }

    // Cancellation + Rollback
    public void cancelBooking(RoomInventory inventory, int reservationId) {

        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Invalid Reservation ID!");
            return;
        }

        // Rollback inventory
        inventory.increaseAvailability(r.getRoom().getType());

        // Remove reservation
        history.removeReservation(reservationId);

        System.out.println("Booking CANCELLED for ID: " + reservationId);
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        // Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 1);

        Room room = new Room("Single", 1000);

        BookingHistory history = new BookingHistory();
        ReservationService service = new ReservationService(history);

        // Booking
        Reservation r1 = service.bookRoom(inventory, room);

        inventory.displayInventory();

        // Cancellation
        if (r1 != null) {
            service.cancelBooking(inventory, r1.getReservationId());
        }

        // Final state
        inventory.displayInventory();
        history.showAll();
    }