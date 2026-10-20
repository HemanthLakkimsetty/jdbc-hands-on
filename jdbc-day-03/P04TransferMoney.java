import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class P04TransferMoney {

    public static void main(String[] args) throws SQLException{
        Connection con=createConnection();

        createBankAccounts(con);

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the no of bank accounts going to open:");
        int n=Integer.parseInt(sc.nextLine());

        for(int i=1;i<=n;i++){
            System.out.println("Eneter the account number:");
            int accountId=Integer.parseInt(sc.nextLine());

            System.out.println("Enter the userName");
            String username=sc.nextLine();

            System.out.println("Enter the balance: ");
            int balance=Integer.parseInt(sc.nextLine());

            insertUserDetails(con, accountId, username, balance);

        }
        
        System.out.println("Eneter the sender account number:");
        int accountId1=Integer.parseInt(sc.nextLine());

        System.out.println("Enter the  reciever account number:");
        int accountId2=Integer.parseInt(sc.nextLine());

        System.out.println("Enter the balance: ");
        int amount=Integer.parseInt(sc.nextLine());

        transferMoney(con, accountId1, accountId2, amount);

        sc.close();

    }
    //establish db connection
    private static Connection createConnection() throws SQLException{
        String host = "<YOUR_HOST>";
        String port = "<YOUR_PORT>";
        String database = "<YOUR_DATABASE>";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?ssl-mode=REQUIRED";

        String username = "<YOUR_USERNAME>";
        String password = "<YOUR_PASSWORD>";

        return DriverManager.getConnection(url,username,password);

    }
    
    //create a tables as accounts
    private static void createBankAccounts(Connection connection){
        String sqlQuery="CREATE TABLE bank_accounts (account_id INT PRIMARY KEY,holder_name VARCHAR(100) NOT NULL,balance DECIMAL(10,2) NOT NULL);";

        try(PreparedStatement createTableStatement=connection.prepareStatement(sqlQuery)){
            createTableStatement.executeUpdate();
            System.out.println("bank_accounts table is created !");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    //insert values into table
    private static void insertUserDetails(Connection connection,int accountId, String holderName,double balance){
        String sqlQuery="INSERT INTO bank_accounts(account_id,holder_name,balance) VALUES (?, ?, ?);";

        try(PreparedStatement insertUserDetailsStatement=connection.prepareStatement(sqlQuery)){
            insertUserDetailsStatement.setInt(1,accountId);
            insertUserDetailsStatement.setString(2,holderName);
            insertUserDetailsStatement.setDouble(3,balance);

            int rowsAffected=insertUserDetailsStatement.executeUpdate();

            if(rowsAffected==1){
                System.out.println("Bank Account created with "+holderName);
            }else{
                System.out.println("Failed to inserted the details");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void transferMoney(Connection connection,int account_id1 ,int account_id2 ,double amount) throws SQLException{

        String balanceQuery="SELECT balance FROM bank_accounts WHERE account_id=?";

        String SqlQuery1="UPDATE bank_accounts SET balance=balance+? WHERE account_id=?";
        
        String SqlQuery2="UPDATE bank_accounts SET balance=balance-? WHERE account_id=?";

        if(amount<=0){
            System.out.println("enter valid amount");
            return;
        }

        if(account_id2==account_id1){
            System.out.println("Both sender and reciever ids are same.Please enter valid account ids");
            return;
        }

        try{

            connection.setAutoCommit(false);

            try(PreparedStatement balanceStatement=connection.prepareStatement(balanceQuery);
            PreparedStatement withdrawStatement=connection.prepareStatement(SqlQuery1);
            PreparedStatement depositStatement=connection.prepareStatement(SqlQuery2)){

                balanceStatement.setInt(1,account_id1);

                try(ResultSet balanceSet=balanceStatement.executeQuery()){
                    if(!balanceSet.next()){
                        System.out.println("Sender account not found "+account_id1);
                        connection.rollback();
                        return;
                    }
                    double balance=balanceSet.getDouble("balance");

                    if(balance<=0 || balance<amount){
                        System.out.println("Insufficient balance "+account_id1);
                        connection.rollback();
                        return;
                    }

                    withdrawStatement.setDouble(1,amount);
                    withdrawStatement.setInt(2,account_id1);

                    int rowsWithdrawn=withdrawStatement.executeUpdate();

                    if(rowsWithdrawn!=1){
                        System.out.println("Transcation failed from "+account_id1);
                        connection.rollback();
                        return;
                    }

                    depositStatement.setDouble(1, amount);
                    depositStatement.setInt(2,account_id2);

                    
                    int rowsDeposit=depositStatement.executeUpdate();

                    if(rowsDeposit!=1){
                        System.out.println("Transcation failed from"+account_id2);
                        connection.rollback();
                        return;
                    }

                    connection.commit();
                    System.out.println("Transaction successful");
                }catch(SQLException e){
                    connection.rollback();
                    throw e;
                }
        }catch(SQLException e){
            e.printStackTrace();
        }

        }finally{
            try{
                connection.setAutoCommit(true);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }


}
