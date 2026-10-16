# Day 2 — JDBC CRUD Operations and PreparedStatement Practice

**Focus:** Create, read, update, and delete records; bind query parameters safely; use filtering, sorting, and aggregate queries.

**Environment:** Java, Maven, JDBC, MySQL  
**Prerequisite:** Complete the Day 1 JDBC connection and `students` table exercises.

---

## Problem 1: Insert a Student Using `PreparedStatement`

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** Parameter binding, `INSERT`

### Problem statement

Read a student's ID, name, age, and course from the console, then insert the record into the `students` table using `PreparedStatement`.

Use this parameterized SQL statement:

```sql
INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?);
```

### Sample input

```text
Enter ID: 107
Enter name: Neha
Enter age: 21
Enter course: JDBC
```

### Expected output

```text
Student inserted successfully
Rows affected: 1
```

### Requirements

- Bind values with `setInt()` and `setString()`.
- Do not concatenate user input into the SQL query.
- Execute the insert with `executeUpdate()`.
- Print the affected-row count.

---

## Problem 2: Search for a Student by ID Using `PreparedStatement`

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** Parameterized `SELECT`, `ResultSet`

### Problem statement

Read a student ID from the console and retrieve the complete matching record from the `students` table using `PreparedStatement`.

### Sample input

```text
Enter student ID: 105
```

### Expected output

```text
ID: 105
Name: Priya
Age: 20
Course: Java
```

If the ID does not exist, display:

```text
Student not found
```

### Requirements

- Use `SELECT id, name, age, course FROM students WHERE id = ?`.
- Bind the ID using `setInt()`.
- Execute the query with `executeQuery()`.
- Use `ResultSet` to read and display the record.

---

## Problem 3: Update a Student's Age

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** `UPDATE`, `PreparedStatement`

### Problem statement

Read a student ID and a new age from the console. Update only the age of the student with that ID.

### Sample input

```text
Student ID: 102
New age: 22
```

### Expected output

If a record is updated:

```text
Student age updated successfully
```

If no record matches:

```text
No student found with that ID
```

### Requirements

- Use a parameterized query similar to `UPDATE students SET age = ? WHERE id = ?`.
- Bind both parameters with the appropriate setter methods.
- Check the number of rows returned by `executeUpdate()`.
- Never omit the `WHERE` clause.

---

## Problem 4: Update a Student's Course

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** `UPDATE`, parameter binding

### Problem statement

Read a student ID and a new course name. Update the course for that student using `PreparedStatement`.

### Sample input

```text
Student ID: 101
New course: Advanced Java
```

### Expected output

```text
Student course updated successfully
```

### Requirements

- Use a parameterized `UPDATE` query.
- Bind the new course name and student ID.
- Check the affected-row count.
- Retrieve the student's record after the update to verify the new course value.
- If no row matches, display `No student found with that ID`.

---

## Problem 5: Delete a Student by ID

**Difficulty:** Easy  
**SQL type:** DML  
**Topics:** `DELETE`, `PreparedStatement`

### Problem statement

Read a student ID from the console and delete only the matching student record.

### Sample input

```text
Enter student ID to delete: 104
```

### Expected output

If a row is deleted:

```text
Student deleted successfully
```

If no row matches:

```text
No student found with that ID
```

### Requirements

- Use `DELETE FROM students WHERE id = ?`.
- Bind the ID using `setInt()`.
- Check the affected-row count.
- Verify the deletion with a `SELECT` query.
- Do not omit the `WHERE` clause.

---

## Problem 6: Filter Students by Age

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** `WHERE`, comparison operators, `ORDER BY`

### Problem statement

Read a minimum age and list students whose age is greater than the entered value. Sort the results by age in descending order.

### Sample input

```text
Enter minimum age: 20
```

### Example output

```text
Students older than 20:
102 Ravi 22 JDBC
106 Kiran 23 JDBC
```

*The actual results depend on the records in your database.*

### Requirements

- Use a parameterized query.
- Use the condition `age > ?`.
- Sort with `ORDER BY age DESC`.
- Display all matching students.
- If no students match, display `No students found`.

---

## Problem 7: Search Students by Course

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** `WHERE`, `PreparedStatement`, `LIKE`

### Problem statement

Read a course name and display all students enrolled in that course.

### Sample input

```text
Enter course: Java
```

### Example output

```text
Students enrolled in Java:
101 Ananya 20 Java
103 Meera 19 Java
105 Priya 20 Java
```

*The actual results depend on the records in your database.*

### Requirements

- Use `WHERE course = ?`.
- Bind the course name using `setString()`.
- Display all matching records.

### Extension

Add a second search option that finds course names beginning with a supplied prefix using `LIKE`.

For example, if the user enters `Ja`, search for course names matching `Ja%`. Bind the pattern as a parameter rather than concatenating user input into the SQL statement.

---

## Problem 8: Sort and Limit Student Records

**Difficulty:** Easy  
**SQL type:** DQL  
**Topics:** `ORDER BY`, `LIMIT`

### Problem statement

Write three separate JDBC queries to retrieve student records in different orders.

### Requirements

1. Display all students in alphabetical order by name using `ORDER BY name ASC`.
2. Display the three oldest students using `ORDER BY age DESC LIMIT 3`.
3. Display the five records with the highest IDs using `ORDER BY id DESC LIMIT 5`.

### Expected behavior

Each query should display the matching records in the requested order. The results depend on the data currently stored in the `students` table.

---

## Problem 9: Calculate Student Statistics

**Difficulty:** Intermediate  
**SQL type:** DQL  
**Topics:** Aggregate functions, `COUNT`, `AVG`, `MIN`, `MAX`, `GROUP BY`

### Problem statement

Write a Java program that retrieves and displays summary statistics for the `students` table.

### Required statistics

1. Total number of students using `COUNT(*)`.
2. Average student age using `AVG(age)`.
3. Minimum student age using `MIN(age)`.
4. Maximum student age using `MAX(age)`.
5. Number of students enrolled in each course using `GROUP BY course`.

### Example output

```text
Total students: 6
Average age: 20.83
Minimum age: 19
Maximum age: 23

Students per course:
Java: 3
JDBC: 2
SQL: 1
```

*The example values are illustrative; your output depends on the data in your database.*

### Requirements

- Use `executeQuery()` for each `SELECT` query.
- Retrieve aggregate values through `ResultSet`.
- Format the average age to two decimal places.
- Use a grouped query for the student count per course.
- Consider how your program should display results if the table is empty.

---

# Day 2 Completion Checklist

- [ ] Problem 1: Insert a student using `PreparedStatement`
- [ ] Problem 2: Search by ID
- [ ] Problem 3: Update a student's age
- [ ] Problem 4: Update a student's course
- [ ] Problem 5: Delete a student by ID
- [ ] Problem 6: Filter students by age and sort results
- [ ] Problem 7: Search by course and use `LIKE`
- [ ] Problem 8: Sort and limit records
- [ ] Problem 9: Calculate student statistics

## Practice rules

1. Attempt each problem before checking a solution.
2. Use `PreparedStatement` for user-supplied values.
3. Check affected-row counts for insert, update, and delete operations.
4. Verify changes by querying the database.
5. Keep database credentials out of source control.
6. Use try-with-resources to close JDBC resources reliably; exception handling is not a topic in this practice set.
