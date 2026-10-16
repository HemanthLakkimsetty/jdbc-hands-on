import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P08SortAndLimit {
    public static void main(String[] args) throws SQLException{
        Scanner sc=new Scanner(System.in);
        int val=Integer.parseInt(sc.nextLine());
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        //Display students in alphabetical order
        studentsInAlphabeticalOrder(url,username,password);
        System.out.println();
        //Display 3 oldest students
        oldestStudents(url,username,password,val);

        sc.close();
    }


    static void studentsInAlphabeticalOrder(String url, String username, String password) {

    // Only allow valid column names
    Scanner sc = new Scanner(System.in);

    System.out.println("Sort students by (name, age, course):");
    String column = sc.nextLine().trim().toLowerCase();
    sc.close();    if (!column.equals("name")
            && !column.equals("age")
            && !column.equals("course")) {
        System.out.println("Invalid column name!");
        return;
    }

    String sqlQuery =
            "SELECT * FROM students ORDER BY " + column + " ASC";

    try (Connection connection =DriverManager.getConnection(url, username, password);
        PreparedStatement ps =connection.prepareStatement(sqlQuery);
        ResultSet rs = ps.executeQuery()) {

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println(
                    rs.getInt("id") + " " +
                    rs.getString("name") + " " +
                    rs.getInt("age") + " " +
                    rs.getString("course"));
        }

        if (!found) {
            System.out.println("No students found.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


static void oldestStudents(
        String url, String username, String password, int val) {

    if (val <= 0) {
        System.out.println("Please enter a limit greater than zero.");
        return;
    }

    String sqlQuery =
            "SELECT * FROM students ORDER BY age DESC LIMIT ?";

    try (Connection connection =DriverManager.getConnection(url, username, password);
        PreparedStatement ps =connection.prepareStatement(sqlQuery)) {

        ps.setInt(1, val);

        try (ResultSet rs = ps.executeQuery()) {
            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                        rs.getInt("id") + " " +
                        rs.getString("name") + " " +
                        rs.getInt("age") + " " +
                        rs.getString("course"));
            }

            if (!found) {
                System.out.println("No students found.");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
