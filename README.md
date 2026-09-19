# Parking Slot Reservation — Spring Boot REST API

A Spring Boot reimplementation of the Parking Slot Reservation & Management System, covering full CRUD and business logic for three entities: **Facility**, **ParkingSlot**, and **Reservation**.

> Assignment 4 — Spring Boot + Postman integration of a previously-built system.

## Links

- **GitHub repository:** `[TODO: add your GitHub repo link here]`
- **Video walkthrough (Google Vid):** `[TODO: add your video link here]`

## Tech stack

| Layer | Technology |
|---|---|
| Language / runtime | Java 17 |
| Framework | Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA) |
| Database | PostgreSQL |
| Validation | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| Boilerplate | Lombok |
| Build tool | Maven (wrapper included, `./mvnw`) |
| API testing | Postman (collection included in `postman/`) |

## Domain model

```
Facility (1) ──< (N) ParkingSlot (1) ──< (N) Reservation
```

- **Facility** — a parking site with a name, address, city, and a hard capacity (`totalSlots`).
- **ParkingSlot** — a bookable spot inside a facility: slot number, vehicle type (`CAR`, `MOTORCYCLE`, `TRUCK`), hourly price, and status (`AVAILABLE`, `RESERVED`, `OCCUPIED`).
- **Reservation** — a customer's booking of a slot for a time window: customer name/email, vehicle plate, start/end time, status (`CONFIRMED`, `CANCELLED`, `COMPLETED`), and a computed `totalAmount`.

## Business rules implemented

**Facility**
- A facility's `totalSlots` must be greater than zero and cannot be reduced below the number of slots already registered.
- A facility cannot be deleted while it still has parking slots linked to it (`409 Conflict`).
- Facility names must be unique.

**ParkingSlot**
- A slot must reference an existing facility (`404` otherwise).
- A facility cannot have more slots created than its `totalSlots` capacity (`409 Conflict`).
- Slot numbers must be unique within their facility.
- A slot cannot be deleted while it has an active (`CONFIRMED`) reservation (`409 Conflict`).
- `GET /api/slots/available` supports filtering by facility and/or vehicle type.

**Reservation**
- `endTime` must be after `startTime` (`400 Bad Request`).
- Double-booking prevention: a new or rescheduled reservation is rejected with `409 Conflict` if it overlaps an existing `CONFIRMED` reservation on the same slot.
- `totalAmount` is computed automatically as `slot.price × duration (hours)`.
- Creating a reservation flips its slot to `RESERVED`; cancelling (`PATCH /api/reservations/{id}/cancel`) flips it back to `AVAILABLE`.
- Only a `CONFIRMED` reservation can be rescheduled or cancelled; acting on an already-cancelled/completed reservation returns `409 Conflict`.
- `GET /api/reservations/facility/{facilityId}` scopes reservations to a facility.

All validation errors return `400` with a `fieldErrors` map; not-found resources return `404`; business-rule violations return `409`. See `GlobalExceptionHandler` for the consistent `ErrorResponse` shape.

## Project structure

Feature-based packages, each with its own `domain` / `dto` / `repository` / `service` / `controller`:

```
auca.ac.rw.parkinkslotReservation/
├── base/          BaseEntity (UUID id + audit timestamps)
├── exception/     Custom exceptions + @RestControllerAdvice
├── facility/
├── slot/
├── reservation/
└── config/        DataSeeder (seeds sample data on first run)
```

## Setup & running

### 1. Create the database

```bash
sudo -u postgres psql -c "CREATE DATABASE parking_slot_reservation;"
```

### 2. Configure credentials

Edit `src/main/resources/application.properties` if your PostgreSQL username/password differ from the defaults:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_slot_reservation
spring.datasource.username=postgres
spring.datasource.password=3002
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. On first run (empty database) it seeds two sample facilities and five parking slots so the API has data to work with immediately.

## API endpoints

### Facility — `/api/facilities`

| Method | Path | Description |
|---|---|---|
| POST | `/api/facilities` | Create a facility |
| GET | `/api/facilities` | List all facilities |
| GET | `/api/facilities/{id}` | Get a facility by id |
| PUT | `/api/facilities/{id}` | Update a facility |
| DELETE | `/api/facilities/{id}` | Delete a facility (blocked if it has slots) |

### ParkingSlot — `/api/slots`

| Method | Path | Description |
|---|---|---|
| POST | `/api/slots` | Create a slot |
| GET | `/api/slots` | List all slots |
| GET | `/api/slots/{id}` | Get a slot by id |
| GET | `/api/slots/facility/{facilityId}` | List slots for a facility |
| GET | `/api/slots/available?facilityId=&vehicleType=` | List available slots (filters optional) |
| PUT | `/api/slots/{id}` | Update a slot |
| DELETE | `/api/slots/{id}` | Delete a slot (blocked if it has an active reservation) |

### Reservation — `/api/reservations`

| Method | Path | Description |
|---|---|---|
| POST | `/api/reservations` | Create a reservation (double-booking checked) |
| GET | `/api/reservations` | List all reservations |
| GET | `/api/reservations/{id}` | Get a reservation by id |
| GET | `/api/reservations/facility/{facilityId}` | List reservations for a facility |
| PUT | `/api/reservations/{id}` | Reschedule a reservation |
| PATCH | `/api/reservations/{id}/cancel` | Cancel a reservation and free its slot |
| DELETE | `/api/reservations/{id}` | Delete a reservation |

## Testing with Postman

Import `postman/ParkingSlotReservation.postman_collection.json` into Postman. It's organized into **Facility**, **ParkingSlot**, and **Reservation** folders. Run the requests within each folder top-to-bottom — `Create` requests capture their generated id into collection variables (`facilityId`, `slotId`, `reservationId`) that subsequent requests reuse. The collection also includes requests that exercise each business rule (capacity limits, duplicate names/slot numbers, overlapping reservations, invalid time ranges, blocked deletes) and asserts the expected status code via test scripts.
# ParkingSlotReservation_postman
