package auca.ac.rw.parkinkslotReservation.slot.service;

import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotRequest;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotResponse;

import java.util.List;
import java.util.UUID;

public interface ParkingSlotService {

    ParkingSlotResponse create(ParkingSlotRequest request);

    ParkingSlotResponse update(UUID id, ParkingSlotRequest request);

    ParkingSlotResponse findById(UUID id);

    List<ParkingSlotResponse> findAll();

    List<ParkingSlotResponse> findByFacility(UUID facilityId);

    List<ParkingSlotResponse> findAvailable(UUID facilityId, VehicleType vehicleType);

    void delete(UUID id);
}
