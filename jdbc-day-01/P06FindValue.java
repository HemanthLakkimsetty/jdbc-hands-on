import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class P06FindValue {
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

        String sqlQuery="SELECT id,name,age,course FROM students WHERE id=109";


        //Instead of closing the connections manually we can go with try-with-resource

        try(Connection connection=DriverManager.getConnection(url,username,password);

        Statement statement=connection.createStatement()
        //here after program is executed connection closed automatically
        ){

        ResultSet resultSet=statement.executeQuery(sqlQuery);
        if(resultSet.next()){
            int id=resultSet.getInt("id");
            String name=resultSet.getString("name");
            int age=resultSet.getInt("age");
            String course=resultSet.getString("course");

            System.out.println("| "+id+" | "+name+" | "+age+" | "+course+" | ");
        }else{
            System.out.println("NO STUDENT EXSIST !");
        }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}