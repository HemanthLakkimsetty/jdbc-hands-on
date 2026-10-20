import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

/** console input and output application logic */
public class ConsoleUI {
    private final Scanner input = new Scanner(System.in);

    public void printMenu() {
        System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
        System.out.println("1. Add room\n2. List rooms\n3. Register guest");
        System.out.println("4. Search available rooms\n5. Make reservation");
        System.out.println("6. View reservation\n7. Guest reservation history");
        System.out.println("8. Cancel reservation\n9. Exit");
    }

    public int readInt(String prompt) {
        return Integer.parseInt(readText(prompt));
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    public LocalDate readDate(String prompt) {
        return LocalDate.parse(readText(prompt));
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.err.println(message);
    }

    public void showRoomHeader() {
        System.out.println("ID | Type | Price/night");
    }

    public void showRoom(ResultSet room) throws SQLException {
        System.out.printf("%d | %s | %s%n", room.getInt("room_id"),
                room.getString("room_type"), room.getBigDecimal("price_per_night"));
    }

    public void showAvailableRoom(ResultSet room) throws SQLException {
        System.out.printf("Room %d | %s | %s per night%n", room.getInt("room_id"),
                room.getString("room_type"), room.getBigDecimal("price_per_night"));
    }

    public void showReservation(ResultSet reservation) throws SQLException {
        System.out.printf("%nReservation %d | %s | phone: %s%nRoom %d (%s) | rate: %s%n%s to %s | total: %s%n",
                reservation.getInt("reservation_id"), reservation.getString("full_name"),
                reservation.getString("phone"), reservation.getInt("room_id"),
                reservation.getString("room_type"), reservation.getBigDecimal("price_per_night"),
                reservation.getDate("check_in"), reservation.getDate("check_out"),
                reservation.getBigDecimal("total_amount"));
    }

    public void showReservationCreated(int id, long nights, BigDecimal total) {
        System.out.printf("Reservation created. ID: %d | Nights: %d | Total: %s%n", id, nights, total);
    }
}
