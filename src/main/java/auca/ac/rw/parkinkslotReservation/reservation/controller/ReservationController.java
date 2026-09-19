package auca.ac.rw.parkinkslotReservation.reservation.controller;

import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationRequest;
import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationResponse;
import auca.ac.rw.parkinkslotReservation.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable UUID id, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<ReservationResponse>> findByFacility(@PathVariable UUID facilityId) {
        return ResponseEntity.ok(reservationService.findByFacility(facilityId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
