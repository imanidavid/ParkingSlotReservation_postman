package auca.ac.rw.parkinkslotReservation.slot.service;

import auca.ac.rw.parkinkslotReservation.exception.BusinessRuleException;
import auca.ac.rw.parkinkslotReservation.exception.ResourceNotFoundException;
import auca.ac.rw.parkinkslotReservation.facility.domain.Facility;
import auca.ac.rw.parkinkslotReservation.facility.repository.FacilityRepository;
import auca.ac.rw.parkinkslotReservation.reservation.domain.ReservationStatus;
import auca.ac.rw.parkinkslotReservation.reservation.repository.ReservationRepository;
import auca.ac.rw.parkinkslotReservation.slot.domain.ParkingSlot;
import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotRequest;
import auca.ac.rw.parkinkslotReservation.slot.dto.ParkingSlotResponse;
import auca.ac.rw.parkinkslotReservation.slot.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ParkingSlotResponse create(ParkingSlotRequest request) {
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + request.getFacilityId()));

        if (parkingSlotRepository.existsBySlotNumberIgnoreCaseAndFacilityId(request.getSlotNumber(), facility.getId())) {
            throw new BusinessRuleException(
                    "Slot number '" + request.getSlotNumber() + "' already exists in this facility");
        }

        long currentSlotCount = parkingSlotRepository.countByFacilityId(facility.getId());
        if (currentSlotCount >= facility.getTotalSlots()) {
            throw new BusinessRuleException(
                    "Facility '" + facility.getName() + "' has reached its capacity of " + facility.getTotalSlots() + " slots");
        }

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber(request.getSlotNumber());
        slot.setVehicleType(request.getVehicleType());
        slot.setPrice(request.getPrice());
        slot.setFacility(facility);
        slot.setStatus(request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE);

        return toResponse(parkingSlotRepository.saveAndFlush(slot));
    }

    @Override
    @Transactional
    public ParkingSlotResponse update(UUID id, ParkingSlotRequest request) {
        ParkingSlot slot = getOrThrow(id);
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + request.getFacilityId()));

        if (parkingSlotRepository.existsBySlotNumberIgnoreCaseAndFacilityIdAndIdNot(
                request.getSlotNumber(), facility.getId(), id)) {
            throw new BusinessRuleException(
                    "Slot number '" + request.getSlotNumber() + "' already exists in this facility");
        }

        boolean facilityChanged = !slot.getFacility().getId().equals(facility.getId());
        if (facilityChanged) {
            long currentSlotCount = parkingSlotRepository.countByFacilityId(facility.getId());
            if (currentSlotCount >= facility.getTotalSlots()) {
                throw new BusinessRuleException(
                        "Facility '" + facility.getName() + "' has reached its capacity of " + facility.getTotalSlots() + " slots");
            }
        }

        slot.setSlotNumber(request.getSlotNumber());
        slot.setVehicleType(request.getVehicleType());
        slot.setPrice(request.getPrice());
        slot.setFacility(facility);
        if (request.getStatus() != null) {
            slot.setStatus(request.getStatus());
        }

        return toResponse(parkingSlotRepository.saveAndFlush(slot));
    }

    @Override
    public ParkingSlotResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<ParkingSlotResponse> findAll() {
        return parkingSlotRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ParkingSlotResponse> findByFacility(UUID facilityId) {
        if (!facilityRepository.existsById(facilityId)) {
            throw new ResourceNotFoundException("Facility not found with id: " + facilityId);
        }
        return parkingSlotRepository.findByFacilityId(facilityId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<ParkingSlotResponse> findAvailable(UUID facilityId, VehicleType vehicleType) {
        List<ParkingSlot> slots;
        if (facilityId != null && vehicleType != null) {
            slots = parkingSlotRepository.findByFacilityIdAndStatusAndVehicleType(facilityId, SlotStatus.AVAILABLE, vehicleType);
        } else if (facilityId != null) {
            slots = parkingSlotRepository.findByFacilityIdAndStatus(facilityId, SlotStatus.AVAILABLE);
        } else if (vehicleType != null) {
            slots = parkingSlotRepository.findByStatusAndVehicleType(SlotStatus.AVAILABLE, vehicleType);
        } else {
            slots = parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE);
        }
        return slots.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        ParkingSlot slot = getOrThrow(id);
        if (reservationRepository.existsBySlotIdAndStatus(id, ReservationStatus.CONFIRMED)) {
            throw new BusinessRuleException(
                    "Cannot delete slot '" + slot.getSlotNumber() + "' while it has an active reservation");
        }
        parkingSlotRepository.delete(slot);
    }

    private ParkingSlot getOrThrow(UUID id) {
        return parkingSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with id: " + id));
    }

    private ParkingSlotResponse toResponse(ParkingSlot slot) {
        return ParkingSlotResponse.builder()
                .id(slot.getId())
                .slotNumber(slot.getSlotNumber())
                .vehicleType(slot.getVehicleType())
                .status(slot.getStatus())
                .price(slot.getPrice())
                .facilityId(slot.getFacility().getId())
                .facilityName(slot.getFacility().getName())
                .createdAt(slot.getCreatedAt())
                .updatedAt(slot.getUpdatedAt())
                .build();
    }
}
