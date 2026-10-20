import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Service {
    private final BankOperations bankOperations;

    public Service(BankOperations bankOperations) { this.bankOperations = bankOperations; }

    public int createAccount(String name) throws SQLException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Account holder name is required.");
        return bankOperations.createAccount(name.trim());
    }

    public Account account(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Account ID must be positive.");
        return bankOperations.findAccount(id);
    }

    public BigDecimal deposit(int id, BigDecimal amount) throws SQLException {
        validateAmount(amount); return bankOperations.deposit(id, amount);
    }

    public BigDecimal withdraw(int id, BigDecimal amount) throws SQLException {
        validateAmount(amount); return bankOperations.withdraw(id, amount);
    }

    public void transfer(int fromId, int toId, BigDecimal amount) throws SQLException {
        if (fromId <= 0 || toId <= 0) throw new IllegalArgumentException("Account IDs must be positive.");
        validateAmount(amount); bankOperations.transfer(fromId, toId, amount);
    }

    public List<Transaction> history(int id) throws SQLException {
        if (account(id) == null) throw new IllegalArgumentException("Account not found.");
        return bankOperations.history(id);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount.scale() > 2) throw new IllegalArgumentException("Amount must have at most two decimal places.");
        if (amount.precision() > 12) throw new IllegalArgumentException("Amount exceeds the supported limit.");
    }
}
