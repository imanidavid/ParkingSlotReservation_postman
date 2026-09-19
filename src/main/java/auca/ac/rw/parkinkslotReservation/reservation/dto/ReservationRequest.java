package auca.ac.rw.parkinkslotReservation.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ReservationRequest {

    @NotNull(message = "Slot id is required")
    private UUID slotId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Customer email must be a valid email address")
    private String customerEmail;

    @NotBlank(message = "Vehicle plate is required")
    private String vehiclePlate;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time cannot be in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
}
