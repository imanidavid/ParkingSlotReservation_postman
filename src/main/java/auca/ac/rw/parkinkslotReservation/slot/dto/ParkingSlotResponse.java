package auca.ac.rw.parkinkslotReservation.slot.dto;

import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ParkingSlotResponse {

    private UUID id;
    private String slotNumber;
    private VehicleType vehicleType;
    private SlotStatus status;
    private Double price;
    private UUID facilityId;
    private String facilityName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
