import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankOperations {
    public int createAccount(String holderName) throws SQLException {
        String sql = "INSERT INTO bank_accounts (holder_name, balance) VALUES (?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, holderName);
            ps.setBigDecimal(2, BigDecimal.ZERO.setScale(2));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            throw new SQLException("Account was created but its ID could not be read.");
        }
    }

    public Account findAccount(int id) throws SQLException {
        String sql = "SELECT account_id, holder_name, balance FROM bank_accounts WHERE account_id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Account(rs.getInt("account_id"), rs.getString("holder_name"), rs.getBigDecimal("balance")) : null;
            }
        }
    }

    public BigDecimal deposit(int accountId, BigDecimal amount) throws SQLException {
        return changeBalance(accountId, amount, "DEPOSIT");
    }

    public BigDecimal withdraw(int accountId, BigDecimal amount) throws SQLException {
        return changeBalance(accountId, amount.negate(), "WITHDRAWAL");
    }

    private BigDecimal changeBalance(int accountId, BigDecimal delta, String type) throws SQLException {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                BigDecimal balance = lockBalance(c, accountId);
                if (balance == null) throw new IllegalArgumentException("Account not found.");
                BigDecimal updated = balance.add(delta);
                if (updated.signum() < 0) throw new IllegalArgumentException("Insufficient funds.");
                updateBalance(c, accountId, updated);
                addTransaction(c, accountId, type, delta.abs());
                c.commit();
                return updated;
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException se) throw se;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new SQLException(e);
            }
        }
    }

    public void transfer(int fromId, int toId, BigDecimal amount) throws SQLException {
        if (fromId == toId) throw new IllegalArgumentException("Choose two different accounts.");
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                // Lock in ID order to avoid deadlocks when opposite transfers happen concurrently.
                int first = Math.min(fromId, toId), second = Math.max(fromId, toId);
                BigDecimal firstBalance = lockBalance(c, first);
                BigDecimal secondBalance = lockBalance(c, second);
                if (firstBalance == null || secondBalance == null) throw new IllegalArgumentException("One or both accounts were not found.");
                BigDecimal fromBalance = fromId == first ? firstBalance : secondBalance;
                BigDecimal toBalance = toId == first ? firstBalance : secondBalance;
                if (fromBalance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds.");
                updateBalance(c, fromId, fromBalance.subtract(amount));
                updateBalance(c, toId, toBalance.add(amount));
                addTransaction(c, fromId, "TRANSFER_OUT", amount);
                addTransaction(c, toId, "TRANSFER_IN", amount);
                c.commit();
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException se) throw se;
                if (e instanceof IllegalArgumentException iae) throw iae;
                throw new SQLException(e);
            }
        }
    }

    public List<Transaction> history(int accountId) throws SQLException {
        String sql = "SELECT transaction_id, account_id, transaction_type, amount, transaction_time " +
                "FROM account_transactions WHERE account_id = ? ORDER BY transaction_time DESC, transaction_id DESC";
        List<Transaction> items = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(new Transaction(rs.getInt("transaction_id"), rs.getInt("account_id"),
                        rs.getString("transaction_type"), rs.getBigDecimal("amount"), rs.getTimestamp("transaction_time").toLocalDateTime()));
            }
        }
        return items;
    }

    private BigDecimal lockBalance(Connection c, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT balance FROM bank_accounts WHERE account_id = ? FOR UPDATE")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : null; }
        }
    }

    private void updateBalance(Connection c, int id, BigDecimal balance) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE bank_accounts SET balance = ? WHERE account_id = ?")) {
            ps.setBigDecimal(1, balance); ps.setInt(2, id); ps.executeUpdate();
        }
    }

    private void addTransaction(Connection c, int id, String type, BigDecimal amount) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO account_transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)")) {
            ps.setInt(1, id); ps.setString(2, type); ps.setBigDecimal(3, amount); ps.executeUpdate();
        }
    }
}
