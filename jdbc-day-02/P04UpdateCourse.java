import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class P04UpdateCourse {
    public static void main(String[] args) throws SQLException{
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        //Sql query with generic ---> '?'
        String sqlQuery="UPDATE students SET course=? WHERE id=?;";

        //try with resource block
        try(Connection connection=DriverManager.getConnection(url,username,password);
        //creatring preparedsatement obj
            PreparedStatement preparedStatement=connection.prepareStatement(sqlQuery);
            //scanner to make interactive
            Scanner sc=new Scanner(System.in)){
                //enter the id
                int id=Integer.parseInt(sc.nextLine());
                //enter course
                String course=sc.nextLine();
                preparedStatement.setInt(2,id);
                preparedStatement.setString(1,course);
                
                int rowsAffected=preparedStatement.executeUpdate();

                if(rowsAffected==1){
                    System.out.println("Course updated successfully");
                }else{
                    System.out.println("no student exists");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }
    }

}
