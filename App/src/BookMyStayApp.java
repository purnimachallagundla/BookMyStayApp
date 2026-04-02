public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");

        int availableRooms = 5;
        int requestedRooms = 2;

        System.out.println("Available Rooms: " + availableRooms);
        System.out.println("Requested Rooms: " + requestedRooms);

        if (requestedRooms <= availableRooms) {
            System.out.println("Rooms are available. Booking confirmed!");
        } else {
            System.out.println("Sorry! Rooms not available.");
        }
    }
}