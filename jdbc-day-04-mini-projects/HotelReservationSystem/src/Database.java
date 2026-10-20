import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String HOST = setting("DB_HOST", "localhost");
    private static final String PORT = setting("DB_PORT", "3306");
    private static final String DATABASE = setting("DB_NAME", "hotel_reservation");
    private static final String USER = setting("DB_USER", "root");
    private static final String PASSWORD = setting("DB_PASSWORD", "");

    private Database() {}

    public static Connection connect() throws SQLException {
        String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
