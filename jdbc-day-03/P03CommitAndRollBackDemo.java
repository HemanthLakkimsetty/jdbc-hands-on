import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class P03CommitAndRollBackDemo {
    public static void main(String[] args) throws SQLException{
        Scanner sc = new Scanner(System.in);
        Connection con=getConnection();
        System.out.println("Enter the id of the student");
        int id=Integer.parseInt(sc.nextLine());
        System.out.println("Enter the course to update");
        String course=sc.nextLine();
        transactionDemo(con,sc, id, course);

    }
    private static Connection getConnection()throws SQLException{

        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        return DriverManager.getConnection(url,username,password);
    }

static void transactionDemo(Connection connection,Scanner sc, int id, String course) throws SQLException{

    connection.setAutoCommit(false);

    String sqlQuery = "UPDATE students SET course=? WHERE id=?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
    ) {

        preparedStatement.setString(1, course);
        preparedStatement.setInt(2, id);

        int rowsUpdated = preparedStatement.executeUpdate();

        if (rowsUpdated == 0) {
            connection.rollback();
            System.out.println("Student not found. Transaction rolled back.");
            return;
        }

        System.out.println("Do you want to commit changes? (y/n)");

        char ch = sc.next().toLowerCase().charAt(0);

        if (ch == 'y') {
            connection.commit();
            System.out.println("Student course changed successfully.");
        } else {
            connection.rollback();
            System.out.println("Transaction rolled back.\nStudent course remains unchanged.");
        }

    } catch (SQLException e) {
        connection.rollback();
        throw e;
    } 
    }
}
