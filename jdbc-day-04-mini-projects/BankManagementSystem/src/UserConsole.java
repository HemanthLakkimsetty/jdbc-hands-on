import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserConsole {
    private final Service service;
    private final Scanner input;

    public UserConsole(Service service, Scanner input) { this.service = service; this.input = input; }

    public void run() {
        System.out.println("Banking System");
        while (true) {
            System.out.println("\n1. Create account\n2. View account\n3. Deposit\n4. Withdraw\n5. Transfer\n6. Transaction history\n0. Exit");
            int choice = readInt("Operation: ");
            try {
                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> viewAccount();
                    case 3 -> changeBalance(true);
                    case 4 -> changeBalance(false);
                    case 5 -> transfer();
                    case 6 -> history();
                    case 0 -> { System.out.println("Goodbye."); return; }
                    default -> System.out.println("Choose an option from 0 to 6.");
                }
            } catch (IllegalArgumentException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createAccount() throws SQLException {
        System.out.print("Account holder name: ");
        int id = service.createAccount(input.nextLine());
        System.out.println("Account created. Account ID: " + id);
    }

    private void viewAccount() throws SQLException {
        Account account = service.account(readInt("Account ID: "));
        if (account == null) { System.out.println("Account not found."); return; }
        System.out.printf("Account ID: %d%nHolder: %s%nBalance: %s%n", account.id(), account.holderName(), money(account.balance()));
    }

    private void changeBalance(boolean deposit) throws SQLException {
        int id = readInt("Account ID: ");
        BigDecimal amount = readAmount("Amount: ");
        BigDecimal balance = deposit ? service.deposit(id, amount) : service.withdraw(id, amount);
        System.out.printf("%s successful. Updated balance: %s%n", deposit ? "Deposit" : "Withdrawal", money(balance));
    }

    private void transfer() throws SQLException {
        int from = readInt("From account ID: ");
        int to = readInt("To account ID: ");
        BigDecimal amount = readAmount("Amount: ");
        service.transfer(from, to, amount);
        System.out.println("Transfer successful.");
        Account source = service.account(from), destination = service.account(to);
        System.out.printf("Updated balances — account %d: %s; account %d: %s%n", from, money(source.balance()), to, money(destination.balance()));
    }

    private void history() throws SQLException {
        int id = readInt("Account ID: ");
        List<Transaction> rows = service.history(id);
        if (rows.isEmpty()) { System.out.println("No transactions found."); return; }
        System.out.printf("%n%-6s %-14s %-12s %s%n", "ID", "Type", "Amount", "Time");
        for (Transaction row : rows)
            System.out.printf("%-6d %-14s %-12s %s%n", row.id(), row.type(), money(row.amount()), row.time());
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(input.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); }
        }
    }

    private BigDecimal readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return new BigDecimal(input.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid amount, for example 500.00."); }
        }
    }

    private String money(BigDecimal amount) { return amount.setScale(2).toPlainString(); }
}
