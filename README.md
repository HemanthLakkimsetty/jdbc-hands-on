# JDBC Hands-On Practice — 4-Day Journey

A four-day Java Database Connectivity (JDBC) practice plan using **Java, IntelliJ IDEA, JDBC, and MySQL**.

## Overview

| Day | Focus | Key Topics | Practice |
|---|---|---|---|
| **Day 1** | JDBC Fundamentals | JDBC setup, MySQL connection, `Connection`, `Statement`, `ResultSet`, basic SQL | Create a table, insert records, retrieve and search student data |
| **Day 2** | CRUD Operations | `PreparedStatement`, insert, select, update, delete, filtering, sorting, aggregates | Build a student management menu |
| **Day 3** | Advanced JDBC | BLOBs, file streams, transactions, batch processing, generated keys, metadata | Store/retrieve images, transfer money, batch insert records |
| **Day 4** | JDBC Mini-Projects | Multi-table CRUD, joins, date handling, transactions, balance validation | Hotel Reservation System and Banking System |

## Projects

### 1. Hotel Reservation System
- Add and list rooms.
- Register guests and search rooms by date.
- Create, view, list, and cancel reservations.
- Calculate the total price and prevent overlapping bookings.
- Use joins to display reservation details.

### 2. Banking System
- Create accounts and view balances.
- Deposit, withdraw, and transfer money.
- Validate amounts and account balances.
- Use transactions for transfers.
- Store transaction history.
- Use Java `BigDecimal` for monetary values.

## Skills Practiced

- Connect Java applications to MySQL using JDBC.
- Perform CRUD operations with `Statement` and `PreparedStatement`.
- Use `ResultSet` and metadata.
- Apply filtering, sorting, and aggregate queries.
- Manage transactions with `commit()` and `rollback()`.
- Use batch processing and generated keys.
- Store and retrieve BLOB data.
- Close JDBC resources with try-with-resources.
- Use joins and validate database operations.

## Technologies

- **Language:** Java
- **IDE:** IntelliJ IDEA
- **Database:** MySQL
- **API:** JDBC / MySQL Connector/J

## Configuration

- Set your database URL, username, and password using environment variables or a local configuration file.
- Keep credentials and private connection URLs out of GitHub.
- Test database changes in MySQL as well as in the console.

## Progress

**Completed:** All four days of the JDBC practice plan.

---

**Goal:** Strengthen JDBC fundamentals and apply them in practical Java console applications.
