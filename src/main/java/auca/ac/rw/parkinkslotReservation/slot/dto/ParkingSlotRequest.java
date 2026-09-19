package auca.ac.rw.parkinkslotReservation.slot.dto;

import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParkingSlotRequest {

    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Facility id is required")
    private UUID facilityId;

    /** Optional manual override (e.g. mark OCCUPIED for a walk-in, or AVAILABLE after maintenance). Defaults to AVAILABLE on create. */
    private SlotStatus status;
}
