package auca.ac.rw.parkinkslotReservation.slot.repository;

import auca.ac.rw.parkinkslotReservation.slot.domain.ParkingSlot;
import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {

    boolean existsByFacilityId(UUID facilityId);

    long countByFacilityId(UUID facilityId);

    boolean existsBySlotNumberIgnoreCaseAndFacilityId(String slotNumber, UUID facilityId);

    boolean existsBySlotNumberIgnoreCaseAndFacilityIdAndIdNot(String slotNumber, UUID facilityId, UUID id);

    List<ParkingSlot> findByFacilityId(UUID facilityId);

    List<ParkingSlot> findByStatus(SlotStatus status);

    List<ParkingSlot> findByFacilityIdAndStatus(UUID facilityId, SlotStatus status);

    List<ParkingSlot> findByFacilityIdAndStatusAndVehicleType(UUID facilityId, SlotStatus status, VehicleType vehicleType);

    List<ParkingSlot> findByStatusAndVehicleType(SlotStatus status, VehicleType vehicleType);
}
