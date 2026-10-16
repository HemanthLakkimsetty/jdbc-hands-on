import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P07SearchByCourse {
    public static void main(String[] args) throws SQLException{
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        //Sql query with generic ---> '?'
        String sqlQuery="SELECT * FROM students WHERE course LIKE ?;";

        //try with resource block
        try(Connection connection=DriverManager.getConnection(url,username,password);
        //creatring preparedsatement obj
            PreparedStatement preparedStatement=connection.prepareStatement(sqlQuery);
            //scanner to make interactive
            Scanner sc=new Scanner(System.in)){
                //enter the id
                String course=sc.nextLine();
                preparedStatement.setString(1,course);
                //another try block for resultset or we can create and close manually
                try(ResultSet resultset=preparedStatement.executeQuery();){
                    //checking resultset have exact 1 only
                    if(resultset.next()){
                        do{
                            
                        int getId=resultset.getInt("id");
                        String getName=resultset.getString("name");
                        int getAge=resultset.getInt("age");
                        String getCourse=resultset.getString("course");

                        System.out.println(getId+" "+getName+" "+getAge+" "+getCourse);
                        }while(resultset.next());
                    }else{
                        System.out.println("No Student Exists with that id");
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
    }
}