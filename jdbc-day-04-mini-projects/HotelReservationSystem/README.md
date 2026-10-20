# Hotel Reservation System

**Difficulty:** Intermediate  
**Topics:** CRUD, multiple tables, joins, date handling, transactions

Build a console-based hotel reservation system with Java 17, JDBC, and a local MySQL database. The application manages rooms, guests, and reservations, and checks reservation dates to prevent overlapping bookings.

## Features

- Add and list rooms.
- Register guests.
- Search for available rooms across a date range.
- Create reservations and calculate the total from the room rate and number of nights.
- View a reservation by ID and list reservations for a guest.
- Cancel reservations.
- Use prepared statements for SQL values and joins to show reservation details with guest and room information.
- Use a transaction to keep reservation creation consistent.

## Rules

- Check-out must be after check-in.
- A room cannot be reserved when its dates overlap an existing reservation.
- Availability is calculated from reservation dates rather than stored as a room flag.

## Example interaction

```text
1. Add room
2. List rooms
3. Register guest
4. Search available rooms
5. Make reservation
6. View reservation
7. Guest reservation history
8. Cancel reservation
9. Exit

Choose an option: 5
Guest ID: 1
Room ID: 201
Check-in (YYYY-MM-DD): 2026-11-10
Check-out (YYYY-MM-DD): 2026-11-12

Reservation created. ID: 1 | Nights: 2 | Total: 250.00
```

## Local MySQL setup

1. Install and start MySQL Server on your computer.
2. Open `schema.sql` in MySQL Workbench or run it with the MySQL command-line client. The script creates the `hotel_reservation` database and its tables.
3. The application connects by default to `localhost:3306` using database `hotel_reservation`, user `root`, and an empty password.
4. If your MySQL setup uses different connection details, set these environment variables in your terminal or IDE run configuration:

   - `DB_HOST` — MySQL server host (default: `localhost`)
   - `DB_PORT` — MySQL server port (default: `3306`)
   - `DB_NAME` — database name (default: `hotel_reservation`)
   - `DB_USER` — MySQL username (default: `root`)
   - `DB_PASSWORD` — MySQL password (default: empty)

## Build and start

The Maven project downloads MySQL Connector/J as a dependency. With Maven installed, build using `mvn package` and start using `mvn exec:java`.

The menu supports adding/listing rooms, guest registration, date-based availability, reservations, reservation lookup/history, and cancellations. Room availability is derived from overlapping reservation dates.
