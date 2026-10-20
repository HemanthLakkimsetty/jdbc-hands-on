import java.sql.Connection;
import java.sql.SQLException;

/** Starts the application and routes menu choices to the appropriate layer. */
public class Main {
    private static final ConsoleUI console = new ConsoleUI();
    private static final ReservationService reservations = new ReservationService();

    public static void main(String[] args) {
        if (!isDatabaseAvailable()) {
            return;
        }

        while (true) {
            console.printMenu();
            try {
                if (!handleChoice(console.readInt("Choose an option: "))) {
                    return;
                }
            } catch (IllegalArgumentException e) {
                console.showMessage("Invalid input: " + e.getMessage());
            } catch (SQLException e) {
                console.showMessage("Database error: " + e.getMessage());
            }
        }
    }
    private static boolean isDatabaseAvailable() {
        try (Connection connection = Database.connect()) {
            if (!connection.isValid(2)) {
                console.showError("Database connection is not valid.");
                return false;
            }
            console.showMessage("Connected to the local Hotel Reservation database.");
            return true;
        } catch (SQLException e) {
            console.showError("Database connection failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean handleChoice(int choice) throws SQLException {
        switch (choice) {
            case 1 -> reservations.addRoom(console);
            case 2 -> reservations.listRooms(console);
            case 3 -> reservations.registerGuest(console);
            case 4 -> reservations.searchRooms(console);
            case 5 -> reservations.makeReservation(console);
            case 6 -> reservations.viewReservation(console);
            case 7 -> reservations.showGuestHistory(console);
            case 8 -> reservations.cancelReservation(console);
            case 9 -> {
                console.showMessage("Goodbye!");
                return false;
            }
            default -> console.showMessage("Invalid option.");
        }
        return true;
    }
}
