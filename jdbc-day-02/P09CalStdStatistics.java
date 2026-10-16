import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class P09CalStdStatistics {
    public static void main(String[] args) {
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        calculateStatistics(url,username,password);
    }
    static void calculateStatistics(
            String url, String username, String password) {

        String sqlQuery =
            "SELECT COUNT(*) AS total_students, " +
            "ROUND(AVG(age), 2) AS average_age, " +
            "MIN(age) AS minimum_age, " +
            "MAX(age) AS maximum_age " +
            "FROM students";

        String courseQuery =
            "SELECT course, COUNT(*) AS total_students " +
            "FROM students GROUP BY course";

        try (Connection connection =DriverManager.getConnection(url, username, password)) {

            // Overall student statistics
            try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sqlQuery)) {

                if (rs.next()) {
                    System.out.println("Total students: "
                        + rs.getInt("total_students"));

                    System.out.printf("Average age: %.2f%n",
                        rs.getDouble("average_age"));

                    System.out.println("Minimum age: "
                        + rs.getInt("minimum_age"));

                    System.out.println("Maximum age: "
                        + rs.getInt("maximum_age"));
                }
            }

            // Number of students in each course
            try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(courseQuery)) {

                System.out.println("\nStudents in each course:");

                while (rs.next()) {
                    System.out.println(
                        rs.getString("course") + ": "
                        + rs.getInt("total_students"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

