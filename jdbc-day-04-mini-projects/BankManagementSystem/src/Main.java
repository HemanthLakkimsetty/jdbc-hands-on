import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new UserConsole(new Service(new BankOperations()), new Scanner(System.in)).run();
    }
}
