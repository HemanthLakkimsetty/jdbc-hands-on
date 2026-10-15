import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class P02CreateTable {
    public static void main(String[] args) throws SQLException{
        //url formation variables
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        //url formation
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        //username and password
        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        //sql queries in sting form due to statement object accepts only string as arguments

        String sqlQuery="CREATE TABLE students(id INT PRIMARY KEY ,name VARCHAR(100),age INT,course VARCHAR(100));";

        //step 1: establish connection using drivermanager

        Connection connection=DriverManager.getConnection(url,username,password);

        System.out.println("Connection is established succefully");

        //step 2: create a statement object using connnection objection

        Statement statement=connection.createStatement();

        System.out.println("Statement object is created succefully");

        /*step 3: Execute sql query and process the data using statement object through
        1. executeUpdate() --> for dml queries
        2. executeQuery() --> for dql quereis

        note: java programmers doesnot have access to perform the ddl commands through jdbc*/

        statement.executeUpdate(sqlQuery);
        System.out.println("TABLE IS CRETAED SUCCESSFULLY");

        //step 4: close the statement interface
        statement.close();
        System.out.println("Statement is closed successfully");
        //step 5: close the connection too
        System.out.println("Connecrtion is closed successfully");
    }
}
