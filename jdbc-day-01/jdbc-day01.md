# Day 1 — JDBC Fundamentals and First Database Connection

**Focus:** JDBC architecture, driver setup, connecting Java to MySQL, `Statement`, `ResultSet`, and basic DDL/DML/DQL.

## Task 1. Verify the JDBC environment

**Difficulty:** Easy  
**Topics:** Java configuration, MySQL JDBC driver

Create a small `Main` class that loads the application configuration and attempts to open a database connection.

### Requirements

- Use `DriverManager.getConnection(...)`.
- Keep connection details outside committed source code.
- Print `Connection established` after a successful connection.
- Close the connection after the check.

### Expected output

```text
Connection established
```

## Task 2. Create a `students` table through JDBC

**Difficulty:** Easy  
**SQL type:** DDL  
**Topics:** `Connection`, `Statement`, `executeUpdate()`

Create a table named `students` with this structure:

| Column | Type | Constraint |
|:--|:--|:--|
| `id` | `INT` | Primary key |
| `name` | `VARCHAR(100)` | Not null |
| `age` | `INT` | |
| `course` | `VARCHAR(100)` | |

### Requirements

- Execute the `CREATE TABLE` statement from Java.
- Make the operation safe to rerun by using `CREATE TABLE IF NOT EXISTS`.
- Do not create the table manually in a database GUI.

### Verification

```sql
DESCRIBE students;
```

## Task 3. Insert one student with `Statement`

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** `INSERT`, `executeUpdate()`

Insert this record into `students`:

| id | name | age | course |
|--:|:--|--:|:--|
| 101 | Ananya | 20 | Java |

### Requirements

- Insert the record using JDBC.
- Print the affected-row count returned by JDBC.

### Expected output

```text
1 student record inserted
```

## Task 4. Insert multiple student records

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** Multiple inserts

Insert the following records. Use unique IDs if your table already contains data.

| id | name | age | course |
|--:|:--|--:|:--|
| 102 | Ravi | 21 | JDBC |
| 103 | Meera | 19 | Java |
| 104 | Arjun | 22 | SQL |
| 105 | Priya | 20 | Java |
| 106 | Kiran | 23 | JDBC |

### Requirements

- Insert all five records.
- Print the total number of rows inserted.
- Verify the records with a `SELECT` query.

## Task 5. Display all students

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** `executeQuery()`, `ResultSet`

Retrieve and display all columns from `students`.

### Expected output format

```text
ID   Name    Age   Course
101  Ananya  20    Java
102  Ravi    21    JDBC
...
```

If the table is empty, display `No students found`.

## Task 6. Find a student by ID

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** `WHERE`, `ResultSet`

Read a student ID from the console and display the matching record.

### Sample input

```text
Enter student ID: 103
```

### Expected output

```text
ID: 103
Name: Meera
Age: 19
Course: Java
```

Display `Student not found` when no row matches.

## Task 7. Day 1 checkpoint

Without copying previous code, create a Java program that:

1. Opens a database connection.
2. Creates a table if it does not exist.
3. Inserts one record.
4. Reads and prints all records.

**Done when:** You can explain what `Connection`, `Statement`, `executeUpdate()`, `executeQuery()`, and `ResultSet` do.

