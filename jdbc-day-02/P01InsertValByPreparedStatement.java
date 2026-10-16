import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class P01InsertValByPreparedStatement {
    public static void main(String[] args) throws SQLException{
        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        String sqlQuery="INSERT INTO students(id,name,age,course) VALUES(?,?,?,?);";

        try(Connection con=DriverManager.getConnection(url,username,password);
        PreparedStatement ps=con.prepareStatement(sqlQuery);
        Scanner sc=new Scanner(System.in)){
            int id=Integer.parseInt(sc.nextLine());
            String name=sc.nextLine();
            int age=Integer.parseInt(sc.nextLine());
            String course=sc.nextLine();
            ps.setInt(1,id);
            ps.setString(2,name);
            ps.setInt(3,age);
            ps.setString(4,course);

            int rowsAffected=ps.executeUpdate();

            if(rowsAffected==1){
                System.out.println(rowsAffected+" inserted into table");
            }else{
                System.out.println("Insertion failed!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
