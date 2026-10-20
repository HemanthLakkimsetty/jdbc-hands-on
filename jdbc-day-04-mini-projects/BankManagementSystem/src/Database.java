import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() { }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("BANK_DB_URL", "jdbc:mysql://localhost:3306/banking_system");
        String user = System.getenv().getOrDefault("BANK_DB_USER", "root");
        String password = System.getenv().getOrDefault("BANK_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }
}
