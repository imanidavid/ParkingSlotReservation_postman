package auca.ac.rw.parkinkslotReservation.facility.controller;

import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityRequest;
import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityResponse;
import auca.ac.rw.parkinkslotReservation.facility.service.FacilityService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    public ResponseEntity<FacilityResponse> create(@Valid @RequestBody FacilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityResponse> update(@PathVariable UUID id, @Valid @RequestBody FacilityRequest request) {
        return ResponseEntity.ok(facilityService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(facilityService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<FacilityResponse>> findAll() {
        return ResponseEntity.ok(facilityService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
