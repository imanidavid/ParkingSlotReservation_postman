package auca.ac.rw.parkinkslotReservation.slot.controller;

import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotRequest;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotResponse;
import auca.ac.rw.parkinkslotReservation.slot.service.ParkingSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    @PostMapping
    public ResponseEntity<ParkingSlotResponse> create(@Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSlotService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSlotResponse> update(@PathVariable UUID id, @Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.ok(parkingSlotService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSlotResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(parkingSlotService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSlotResponse>> findAll() {
        return ResponseEntity.ok(parkingSlotService.findAll());
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<ParkingSlotResponse>> findByFacility(@PathVariable UUID facilityId) {
        return ResponseEntity.ok(parkingSlotService.findByFacility(facilityId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSlotResponse>> findAvailable(
            @RequestParam(required = false) UUID facilityId,
            @RequestParam(required = false) VehicleType vehicleType) {
        return ResponseEntity.ok(parkingSlotService.findAvailable(facilityId, vehicleType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        parkingSlotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
