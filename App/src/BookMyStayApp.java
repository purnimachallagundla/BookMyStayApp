import java.util.*;

// Room Model
class Room {
    private String type;

    public Room(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

// Thread-Safe Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // SYNCHRONIZED → ensures thread safety
    public synchronized boolean bookRoom(String type) {

        int available = inventory.getOrDefault(type, 0);
