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

// Add-On Service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be empty");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Invalid service cost");
        }
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Reservation
class Reservation {
    private static int counter = 1;

    private int reservationId;
    private Room room;
    private List<AddOnService> services;
    private double totalCost;

    public Reservation(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        this.reservationId = counter++;
        this.room = room;
        this.services = new ArrayList<>();
        this.totalCost = room.getPrice();
    }

    public int getReservationId() {
        return reservationId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void addService(AddOnService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        services.add(service);
        totalCost += service.getCost();
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Room: " + room.getType() +
                ", Total Cost: " + totalCost);
    }
}

// Inventory
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be empty");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid room count");
        }
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        if (!inventory.containsKey(type)) {
            throw new IllegalArgumentException("Room type not found: " + type);
        }
        return inventory.get(type);
    }

    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current <= 0) {
            throw new IllegalStateException("No rooms available for: " + type);
        }
        inventory.put(type, current - 1);
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        if (r == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        history.add(r);
    }

    public void showAllBookings() {
        System.out.println("\n--- Booking History ---");
        if (history.isEmpty()) {
            System.out.println("No bookings found.");
        }
        for (Reservation r : history) {
            r.display();
        }
    }
}

// Reservation Service
class ReservationService {

    private BookingHistory history;

    public ReservationService(BookingHistory history) {
        this.history = history;
    }

    public Reservation bookRoom(RoomInventory inventory, Room room) {
        try {
            int available = inventory.getAvailability(room.getType());

            if (available > 0) {
                inventory.reduceAvailability(room.getType());

                Reservation reservation = new Reservation(room);
                history.addReservation(reservation);

                System.out.println("Booking CONFIRMED! ID: " + reservation.getReservationId());
                return reservation;

            } else {
                System.out.println("Booking FAILED! No rooms available.");
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error during booking: " + e.getMessage());
            return null;
        }
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        try {
            RoomInventory inventory = new RoomInventory();
            inventory.addRoomType("Single", 1);

            Room room = new Room("Single", 1000);

            BookingHistory history = new BookingHistory();
            ReservationService service = new ReservationService(history);

            // Valid booking
            Reservation r1 = service.bookRoom(inventory, room);

            // Invalid booking (no rooms left)
            Reservation r2 = service.bookRoom(inventory, room);

            // Invalid service example
            if (r1 != null) {
                r1.addService(new AddOnService("WiFi", 200));
                // Uncomment below to test error
                // r1.addService(new AddOnService("", -50));
            }

            history.showAllBookings();

        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
    }

