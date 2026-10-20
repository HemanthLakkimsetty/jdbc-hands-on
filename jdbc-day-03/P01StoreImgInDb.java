
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;

public class P01StoreImgInDb {

    // 1. Database connection function
    public static Connection getConnection() throws SQLException {

        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        return DriverManager.getConnection(url, username, password);
    }

    // 2. Image path and conversion function
    public static byte[] convertImageToBytes() throws IOException {

        String imagePath = "C:\\Users\\DELL\\Downloads\\Conquer.jpeg";

        try (FileInputStream inputStream =
                    new FileInputStream(imagePath)) {

            return inputStream.readAllBytes();
        }
    }

    // 3. Main function
    public static void main(String[] args) {

        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS img_storing_table (
                    photo_id INT PRIMARY KEY AUTO_INCREMENT,
                    file_name VARCHAR(100) NOT NULL,
                    img LONGBLOB NOT NULL,
                    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        String insertSQL =
                "INSERT INTO img_storing_table (file_name, img) VALUES (?, ?)";

        try (Connection connection = getConnection()) {

            // Create table
            try (PreparedStatement createStatement = connection.prepareStatement(createTableSQL)) {

                createStatement.executeUpdate();
                System.out.println("Table is ready.");
            }

            // Convert image into byte array
            byte[] imageData = convertImageToBytes();

            // Insert image into database
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

                insertStatement.setString(1, "Conquer.jpeg");
                insertStatement.setBytes(2, imageData);

                int rows = insertStatement.executeUpdate();

                if (rows > 0) {
                    System.out.println("Image uploaded successfully!");
                } else {
                    System.out.println("Image upload failed.");
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
