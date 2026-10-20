import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class P05JDBCBatchDemo {
    public static void main(String[] args) throws RuntimeException,SQLException{
        String host = "<YOUR_HOST>";
        String port = "<YOUR_PORT>";
        String database = "<YOUR_DATABASE>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        
        String sql = "INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
        Scanner sc = new Scanner(System.in)) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                System.out.print("Enter number of students: ");
                int n = Integer.parseInt(sc.nextLine());

                for (int i = 0; i < n; i++) {
                    System.out.println("Enter details for student " + (i + 1) + ":");

                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());;

                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Age: ");
                    int age = Integer.parseInt(sc.nextLine());

                    System.out.println("Course: ");
                    String course=sc.nextLine();

                    ps.setInt(1, id);
                    ps.setString(2, name);
                    ps.setInt(3, age);
                    ps.setString(4,course);

                    ps.addBatch();
                }

                int[] counts = ps.executeBatch();

                con.commit();

                int processed = 0;

                for (int count : counts) {
                    if (count == Statement.SUCCESS_NO_INFO) {
                        // Successful execution; exact count is unknown.
                        processed++;
                    } else if (count >= 0) {
                        // Count may be greater than 1.
                        processed += count;
                    }
                }

                System.out.println("Batch executed successfully.");
                System.out.println("Records processed: " + processed);

                con.setAutoCommit(true);

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
