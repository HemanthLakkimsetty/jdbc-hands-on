
# Day 3: Advanced JDBC Concepts — Question Bank

**Focus:** BLOB/image storage, transaction management, batch processing, generated keys, and metadata.

---

## Task 1: Store an Image in MySQL

**Difficulty:** Intermediate  
**SQL type:** DDL and DML  
**Topics:** `BLOB`, `PreparedStatement`, file streams

### Problem Statement

Create a table named `student_photos`:

```sql
CREATE TABLE IF NOT EXISTS student_photos (
    photo_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    image_data LONGBLOB NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);
```

Write a Java program that reads an image file from a local path and stores its bytes using `PreparedStatement` and an input stream.

### Requirements

- Store the file name alongside the image data.
- Use a valid student ID for the foreign key.
- Do not hardcode a machine-specific path into the final application.

---

## Task 2: Retrieve an Image from MySQL

**Difficulty:** Intermediate  
**Topics:** `ResultSet`, BLOB retrieval, file output

### Problem Statement

Read a `photo_id`, retrieve its image bytes, and write the image to an output file.

### Expected Behavior

- Save the image to a user-specified output path.
- Use the stored file name or a safe generated name.
- Open the saved file to verify that it matches the uploaded image.

---

## Task 3: Demonstrate `commit()` and `rollback()`

**Difficulty:** Intermediate  
**SQL type:** DML and TCL  
**Topics:** `setAutoCommit(false)`, `commit()`, `rollback()`

### Problem Statement

Update a student's course inside a transaction.

### Requirements

1. Disable auto-commit.
2. Update the course for one student.
3. Ask the user whether to commit.
4. If the answer is yes, call `commit()`.
5. Otherwise, call `rollback()`.
6. Query the record afterward and show the final course.

### Sample Input

```text
Student ID: 101
New course: Advanced Java
Commit changes? no
```

### Expected Output

```text
Transaction rolled back
Student course remains unchanged
```

---

## Task 4: Transfer Money Between Two Accounts

**Difficulty:** Intermediate  
**SQL type:** DDL, DML, TCL  
**Topics:** Transactions, conditional updates

### Problem Statement

Create a table named `bank_accounts`:

```sql
CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id INT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(12, 2) NOT NULL
);
```

Insert two test accounts and write a transfer operation.

### Requirements

- Read source account, destination account, and transfer amount.
- Reject amounts less than or equal to zero.
- Do not allow a balance to become negative.
- Debit and credit must succeed together.
- Commit only when both account updates affect the expected rows; otherwise roll back.
- Display both balances after a successful transfer.

### Sample Scenario

Account 1 has ₹5,000 and account 2 has ₹2,000. Transfer ₹1,000 from account 1 to account 2.

### Expected Balances

```text
Account 1 balance: 4000.00
Account 2 balance: 3000.00
```

---

## Task 5: Batch Insert Student Records

**Difficulty:** Intermediate  
**SQL type:** DML  
**Topics:** `addBatch()`, `executeBatch()`

### Problem Statement

Read several student records and insert them with a single prepared insert statement.

### Requirements

- Use `addBatch()` for each record.
- Execute all queued records with `executeBatch()`.
- Print the number of records processed based on the returned update counts.
- Do not assume every returned count is exactly `1`.
- Use a transaction if the batch must succeed or fail as a unit.
