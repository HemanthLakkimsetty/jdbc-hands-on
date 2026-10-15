import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class P05DisplayAllValues {
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

        //to dispaly values we use Select statement in sql
        //selected rows translated to java program as they are packed as resultset interface form
        //we also neeed to close the resultset too
        String sqlQuery="SELECT * FROM students";


        //Instead of closing the connections manually we can go with try-with-resource

        try(Connection connection=DriverManager.getConnection(url,username,password);

        Statement statement=connection.createStatement();
        
        ResultSet resultSet=statement.executeQuery(sqlQuery);
        ){
            //we use do while loop for dsplay
            /*if we use while loop and if condition the cursour of the resultSet incremented twice to prevent we use do while */
            if(resultSet.next()){
                System.out.println("| id | name | age | course |");
                do{
                    int id=resultSet.getInt("id");
                    String name=resultSet.getString("name");
                    int age=resultSet.getInt("age");
                    String course=resultSet.getString("course");

                    
                    System.out.println("| "+id+" | "+name+" | "+age+" | "+course+" | ");
                }while(resultSet.next());
            }else{
                System.out.println("DATA PETCHING  ERROR !");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}