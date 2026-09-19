package auca.ac.rw.parkinkslotReservation.reservation.dto;

import auca.ac.rw.parkinkslotReservation.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {

    private UUID id;
    private UUID slotId;
    private String slotNumber;
    private UUID facilityId;
    private String facilityName;
    private String customerName;
    private String customerEmail;
    private String vehiclePlate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
