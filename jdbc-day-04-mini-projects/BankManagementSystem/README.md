## Banking System using JDBC
**Difficulty:** Intermediate
**Topics:** CRUD, `DECIMAL`, transactions, balance validation

Build a banking console application using JDBC. Keep the Java code in small classes for readability instead of putting everything in one file.

### Required features

1. Create an account.
2. View account details and balance.
3. Deposit money.
4. Withdraw money.
5. Transfer money between accounts.
6. Display account transaction history.

### Suggested additional table

```sql
CREATE TABLE IF NOT EXISTS account_transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    transaction_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES bank_accounts(account_id)
);
```

### Rules to implement

- Amounts must be positive.
- A withdrawal must not exceed the available balance.
- A transfer must debit one account and credit the other in a single transaction.
- Record transaction history for deposits, withdrawals, and transfers.
- Use `PreparedStatement` for all values.
- Use `BigDecimal` in Java for monetary amounts rather than `double`.

### Example interaction

```text
Operation: Deposit
Account ID: 1
Amount: 500.00
```

**Expected result:** Show a successful deposit message and the updated balance.
