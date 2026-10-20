import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Contains the reservation workflows, validation, and database operations. */
public class ReservationService {
    public void addRoom(ConsoleUI console) throws SQLException {
        int id = console.readInt("Room ID: ");
        String type = console.readText("Room type: ");
        BigDecimal rate = new BigDecimal(console.readText("Price per night: "));
        validateRoom(type, rate);

        String sql = "INSERT INTO rooms (room_id, room_type, price_per_night) VALUES (?, ?, ?)";
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, type);
            statement.setBigDecimal(3, rate);
            statement.executeUpdate();
            console.showMessage("Room added.");
        }
    }

    private void validateRoom(String type, BigDecimal rate) {
        if (type.isBlank() || rate.signum() < 0) {
            throw new IllegalArgumentException("Room type is required and price cannot be negative.");
        }
    }

    public void listRooms(ConsoleUI console) throws SQLException {
        String sql = "SELECT room_id, room_type, price_per_night FROM rooms ORDER BY room_id";
        try (Connection connection = Database.connect();
            Statement statement = connection.createStatement();
            ResultSet rooms = statement.executeQuery(sql)) {
            console.showRoomHeader();
            while (rooms.next()) {
                console.showRoom(rooms);
            }
        }
    }

    public void registerGuest(ConsoleUI console) throws SQLException {
        String name = console.readText("Guest full name: ");
        String phone = console.readText("Phone (optional): ");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }

        String sql = "INSERT INTO guests (full_name, phone) VALUES (?, ?)";
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            setOptionalPhone(statement, phone);
            statement.executeUpdate();
            showCreatedGuestId(statement, console);
        }
    }

    private void setOptionalPhone(PreparedStatement statement, String phone) throws SQLException {
        if (phone.isBlank()) {
            statement.setNull(2, Types.VARCHAR);
        } else {
            statement.setString(2, phone);
        }
    }

    private void showCreatedGuestId(PreparedStatement statement, ConsoleUI console) throws SQLException {
        try (ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                console.showMessage("Guest registered. ID: " + keys.getInt(1));
            }
        }
    }

    public void searchRooms(ConsoleUI console) throws SQLException {
        LocalDate checkIn = console.readDate("Check-in (YYYY-MM-DD): ");
        LocalDate checkOut = console.readDate("Check-out (YYYY-MM-DD): ");
        validateDates(checkIn, checkOut);

        String sql = "SELECT r.room_id, r.room_type, r.price_per_night FROM rooms r WHERE NOT EXISTS "
                + "(SELECT 1 FROM reservations b WHERE b.room_id=r.room_id AND b.check_in < ? AND b.check_out > ?) "
                + "ORDER BY r.room_id";
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            bindAvailabilityDates(statement, checkIn, checkOut);
            try (ResultSet rooms = statement.executeQuery()) {
                showAvailableRooms(rooms, console);
            }
        }
    }

    private void bindAvailabilityDates(PreparedStatement statement, LocalDate checkIn, LocalDate checkOut)
            throws SQLException {
        statement.setDate(1, Date.valueOf(checkOut));
        statement.setDate(2, Date.valueOf(checkIn));
    }

    private void showAvailableRooms(ResultSet rooms, ConsoleUI console) throws SQLException {
        boolean found = false;
        while (rooms.next()) {
            found = true;
            console.showAvailableRoom(rooms);
        }
        if (!found) {
            console.showMessage("No rooms available for those dates.");
        }
    }

    public void makeReservation(ConsoleUI console) throws SQLException {
        int guestId = console.readInt("Guest ID: ");
        int roomId = console.readInt("Room ID: ");
        LocalDate checkIn = console.readDate("Check-in (YYYY-MM-DD): ");
        LocalDate checkOut = console.readDate("Check-out (YYYY-MM-DD): ");
        validateDates(checkIn, checkOut);

        try (Connection connection = Database.connect()) {
            createReservationTransaction(connection, guestId, roomId, checkIn, checkOut, console);
        }
    }

    private void createReservationTransaction(Connection connection, int guestId, int roomId, LocalDate checkIn, LocalDate checkOut, ConsoleUI console) throws SQLException {
        connection.setAutoCommit(false);
        try {
            BigDecimal rate = findRoomRate(connection, roomId);
            verifyGuestExists(connection, guestId);
            verifyRoomAvailable(connection, roomId, checkIn, checkOut);
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            BigDecimal total = rate.multiply(BigDecimal.valueOf(nights));
            int reservationId = insertReservation(connection, guestId, roomId, checkIn, checkOut, total);
            connection.commit();
            console.showReservationCreated(reservationId, nights, total);
        } catch (SQLException | RuntimeException e) {
            rollback(connection, e);
            throw e;
        }
    }

    private BigDecimal findRoomRate(Connection connection, int roomId) throws SQLException {
        String sql = "SELECT price_per_night FROM rooms WHERE room_id=? FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException("Room ID not found.");
                }
                return result.getBigDecimal(1);
            }
        }
    }

    private void verifyGuestExists(Connection connection, int guestId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT guest_id FROM guests WHERE guest_id=?")) {
            statement.setInt(1, guestId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException("Guest ID not found.");
                }
            }
        }
    }

    private void verifyRoomAvailable(Connection connection, int roomId, LocalDate checkIn, LocalDate checkOut)throws SQLException {
        String sql = "SELECT reservation_id FROM reservations WHERE room_id=? AND check_in < ? "
                + "AND check_out > ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setDate(2, Date.valueOf(checkOut));
            statement.setDate(3, Date.valueOf(checkIn));
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    throw new SQLException("Room is already booked for those dates.");
                }
            }
        }
    }

    private int insertReservation(Connection connection, int guestId, int roomId, LocalDate checkIn, LocalDate checkOut, BigDecimal total) throws SQLException {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in, check_out, total_amount) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, guestId);
            statement.setInt(2, roomId);
            statement.setDate(3, Date.valueOf(checkIn));
            statement.setDate(4, Date.valueOf(checkOut));
            statement.setBigDecimal(5, total);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Could not retrieve reservation ID.");
                }
                return keys.getInt(1);
            }
        }
    }

    private void rollback(Connection connection, Exception cause) {
        try {
            connection.rollback();
        } catch (SQLException rollbackError) {
            cause.addSuppressed(rollbackError);
        }
    }

    public void viewReservation(ConsoleUI console) throws SQLException {
        int id = console.readInt("Reservation ID: ");
        String sql = reservationQuery() + " WHERE b.reservation_id=?";
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    console.showReservation(result);
                } else {
                    console.showMessage("Reservation not found.");
                }
            }
        }
    }

    public void showGuestHistory(ConsoleUI console) throws SQLException {
        int guestId = console.readInt("Guest ID: ");
        String sql = reservationQuery() + " WHERE b.guest_id=? ORDER BY b.check_in";
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, guestId);
            try (ResultSet results = statement.executeQuery()) {
                showReservationHistory(results, console);
            }
        }
    }

    private void showReservationHistory(ResultSet results, ConsoleUI console) throws SQLException {
        boolean found = false;
        while (results.next()) {
            found = true;
            console.showReservation(results);
        }
        if (!found) {
            console.showMessage("No reservations found for this guest.");
        }
    }

    private String reservationQuery() {
        return "SELECT b.reservation_id, g.full_name, g.phone, r.room_id, r.room_type, r.price_per_night, "
                + "b.check_in, b.check_out, b.total_amount FROM reservations b "
                + "JOIN guests g ON b.guest_id=g.guest_id JOIN rooms r ON b.room_id=r.room_id";
    }

    public void cancelReservation(ConsoleUI console) throws SQLException {
        int id = console.readInt("Reservation ID to cancel: ");
        try (Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM reservations WHERE reservation_id=?")) {
            statement.setInt(1, id);
            console.showMessage(statement.executeUpdate() > 0 ? "Reservation cancelled." : "Reservation not found.");
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }
    }
}
