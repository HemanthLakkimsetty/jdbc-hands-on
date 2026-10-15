import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class P04InsertMultiValues {
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

        String sqlQuery="INSERT INTO students VALUES(101,'Ananya',20,'Java'),(102,'Ravi',21,'JDBC'),(103,'Meera',19,'SQL'),(104,'Arjun',22,'SQL'),(105,'Priya',20,'Java'),(106,'Kiran',23,'JDBC');";


        //Instead of closing the connections manually we can go with try-with-resource

        try(Connection connection=DriverManager.getConnection(url,username,password);

        Statement statement=connection.createStatement()
        //here after program is executed connection closed automatically
        ){

        int res=statement.executeUpdate(sqlQuery);
        if(res==5){
            System.out.println("INSERTION  SUCCESSFULLY");
        }else{
            System.out.println("INSERTION FAILED !");
        }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
