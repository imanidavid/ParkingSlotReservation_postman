package auca.ac.rw.parkinkslotReservation.facility.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class FacilityResponse {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private Integer totalSlots;
    private int occupiedSlots;
    private int availableCapacity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
