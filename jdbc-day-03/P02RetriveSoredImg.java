import java.sql.DriverManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class P02RetriveSoredImg {

    private static Connection getConnection() throws SQLException {

        String host = "<YOUR_HOST>";
        String database = "<YOUR_DATABASE>";
        String port = "<YOUR_PORT>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        return DriverManager.getConnection(url,username,password);
        }

    public static void getImage(Connection connection,String imgName,String filePath){
        String sqlQuery="SELECT img FROM img_storing_table WHERE file_name=?";
        try(PreparedStatement getImageStatement=connection.prepareStatement(sqlQuery);){

            getImageStatement.setString(1,imgName);

            ResultSet imageData=getImageStatement.executeQuery();

            if(imageData.next()){
                byte[] imageArray=imageData.getBytes("img");

                String imagePath=filePath+"/ImageName.jpeg";

                OutputStream outputStream=new FileOutputStream(imagePath);

                outputStream.write(imageArray);

                System.out.println("Image retrived succussfully");

                outputStream.close();
            }else{
                System.out.println("Failed to retrive image");
            }
            
            imageData.close();
        }catch(SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        String filePath="C:\\Users\\DELL\\Desktop";
        Connection connection = getConnection();
        String name="king";

        getImage(connection, name, filePath);
        
    }
}
