package auca.ac.rw.parkinkslotReservation.slot.domain;

import auca.ac.rw.parkinkslotReservation.base.BaseEntity;
import auca.ac.rw.parkinkslotReservation.facility.domain.Facility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParkingSlot extends BaseEntity {

    @Column(name = "slot_number", nullable = false, length = 20)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlotStatus status;

    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;
}
