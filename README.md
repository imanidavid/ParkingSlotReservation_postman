# Parking Slot Reservation — Spring Boot REST API

Spring Boot API for Facility, ParkingSlot, and Reservation management (Assignment 4 — Spring Boot + Postman).

## Tech stack

Java 17 · Spring Boot 4.1.1 (Web, JPA) · PostgreSQL · Jakarta Validation · Lombok · Maven (`./mvnw`) · Postman (`postman/`)

## Domain model

```
Facility (1) ──< (N) ParkingSlot (1) ──< (N) Reservation
```

- **Facility** — site with name, address, city, capacity (`totalSlots`).
- **ParkingSlot** — slot number, vehicle type (`CAR`/`MOTORCYCLE`/`TRUCK`), hourly price, status.
- **Reservation** — customer booking with time window, status, computed `totalAmount`.

## Business rules

- Facility: `totalSlots > 0`, not reduced below registered slots, unique name, cannot be deleted with slots.
- Slot: must reference a facility, capped by `totalSlots`, unique number per facility, cannot be deleted with a `CONFIRMED` reservation.
- Reservation: `endTime` after `startTime`, no overlapping bookings on a slot, `totalAmount = price × hours`, slot flips to `RESERVED` on create / `AVAILABLE` on cancel, only `CONFIRMED` can be rescheduled or cancelled.

Validation errors → `400` with `fieldErrors`; not found → `404`; business-rule violations → `409`.

## Setup & running

```bash
sudo -u postgres psql -c "CREATE DATABASE parking_slot_reservation;"
# edit src/main/resources/application.properties if your DB credentials differ
./mvnw spring-boot:run
```

App starts on `http://localhost:8080` and seeds sample data on first run.

## API endpoints

- **Facility** `/api/facilities` — POST, GET, GET `/{id}`, PUT `/{id}`, DELETE `/{id}`.
- **ParkingSlot** `/api/slots` — POST, GET, GET `/{id}`, GET `/facility/{facilityId}`, GET `/available?facilityId=&vehicleType=`, PUT `/{id}`, DELETE `/{id}`.
- **Reservation** `/api/reservations` — POST, GET, GET `/{id}`, GET `/facility/{facilityId}`, PUT `/{id}`, PATCH `/{id}/cancel`, DELETE `/{id}`.

## Testing with Postman

Import `postman/ParkingSlotReservation.postman_collection.json`. Run requests top-to-bottom within each folder; create requests store ids (`facilityId`, `slotId`, `reservationId`) in collection variables for reuse. Business-rule cases assert expected status codes via test scripts.