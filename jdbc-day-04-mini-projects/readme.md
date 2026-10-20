# Day 4: JDBC Mini-Projects

**Focus:** Apply JDBC skills to realistic multi-table applications. Build the projects as console applications first.

---

## Project 1: Hotel Reservation System

**Difficulty:** Intermediate  
**Topics:** CRUD, multiple tables, joins, date handling, transactions

### Problem Statement

Build a console-based hotel reservation system.

### Suggested Tables

```sql
CREATE TABLE IF NOT EXISTS rooms (
    room_id INT PRIMARY KEY,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS guests (
    guest_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
```

### Required Features

1. Add and list rooms.
2. Register a guest.
3. Search available rooms for a date range.
4. Create a reservation.
5. Calculate the total price from the room rate and number of nights.
6. View a reservation by ID.
7. List reservations for a guest.
8. Cancel a reservation.
9. Use a transaction when multiple database changes must remain consistent.

### Rules to Implement

- Check-out must be after check-in.
- Do not reserve a room that conflicts with an existing reservation for the requested dates.
- Use `PreparedStatement` for values.
- Use joins to display reservation details with guest and room information.

### Example Interaction

```text
1. List rooms
2. Register guest
3. Search available rooms
4. Make reservation
5. View reservation
6. Cancel reservation
7. Exit

Choose an option: 4
Guest ID: 1
Room ID: 201
Check-in (YYYY-MM-DD): 2026-11-10
Check-out (YYYY-MM-DD): 2026-11-12
```

### Expected Result

Display reservation ID and total amount if the reservation is created successfully.

---

## Project 2: Banking System Using JDBC

**Difficulty:** Intermediate  
**Topics:** CRUD, `DECIMAL`, transactions, balance validation

### Problem Statement

Build a banking console application using Java and JDBC.

### Required Features

1. Create an account.
2. View account details and balance.
3. Deposit money.
4. Withdraw money.
5. Transfer money between accounts.
6. Display account transaction history.

### Suggested Additional Table

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

### Rules to Implement

- Amounts must be positive.
- A withdrawal must not exceed the available balance.
- A transfer must debit one account and credit the other in a single transaction.
- Record transaction history for deposits, withdrawals, and transfers.
- Use `PreparedStatement` for all values.
- Use `BigDecimal` in Java for monetary amounts rather than `double`.

### Example Interaction

```text
Operation: Deposit
Account ID: 1
Amount: 500.00
```

### Expected Result

Show a successful deposit message and the updated balance.
