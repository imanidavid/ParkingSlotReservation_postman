package auca.ac.rw.parkinkslotReservation.facility.service;

import auca.ac.rw.parkinkslotReservation.exception.BusinessRuleException;
import auca.ac.rw.parkinkslotReservation.exception.ResourceNotFoundException;
import auca.ac.rw.parkinkslotReservation.facility.domain.Facility;
import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityRequest;
import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityResponse;
import auca.ac.rw.parkinkslotReservation.facility.repository.FacilityRepository;
import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    @Override
    @Transactional
    public FacilityResponse create(FacilityRequest request) {
        if (facilityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("A facility named '" + request.getName() + "' already exists");
        }

        Facility facility = new Facility();
        facility.setName(request.getName());
        facility.setAddress(request.getAddress());
        facility.setCity(request.getCity());
        facility.setTotalSlots(request.getTotalSlots());

        return toResponse(facilityRepository.saveAndFlush(facility));
    }

    @Override
    @Transactional
    public FacilityResponse update(UUID id, FacilityRequest request) {
        Facility facility = getOrThrow(id);

        if (facilityRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new BusinessRuleException("A facility named '" + request.getName() + "' already exists");
        }

        long currentSlotCount = parkingSlotRepository.countByFacilityId(id);
        if (request.getTotalSlots() < currentSlotCount) {
            throw new BusinessRuleException(
                    "Cannot set total slots below the " + currentSlotCount + " slot(s) already registered for this facility");
        }

        facility.setName(request.getName());
        facility.setAddress(request.getAddress());
        facility.setCity(request.getCity());
        facility.setTotalSlots(request.getTotalSlots());

        return toResponse(facilityRepository.saveAndFlush(facility));
    }

    @Override
    public FacilityResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<FacilityResponse> findAll() {
        return facilityRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Facility facility = getOrThrow(id);
        if (parkingSlotRepository.existsByFacilityId(id)) {
            throw new BusinessRuleException(
                    "Cannot delete facility '" + facility.getName() + "' while it still has parking slots linked to it");
        }
        facilityRepository.delete(facility);
    }

    private Facility getOrThrow(UUID id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));
    }

    private FacilityResponse toResponse(Facility facility) {
        long occupied = parkingSlotRepository.findByFacilityId(facility.getId()).stream()
                .filter(slot -> slot.getStatus() != SlotStatus.AVAILABLE)
                .count();
        long registered = parkingSlotRepository.countByFacilityId(facility.getId());

        return FacilityResponse.builder()
                .id(facility.getId())
                .name(facility.getName())
                .address(facility.getAddress())
                .city(facility.getCity())
                .totalSlots(facility.getTotalSlots())
                .occupiedSlots((int) occupied)
                .availableCapacity((int) (facility.getTotalSlots() - registered))
                .createdAt(facility.getCreatedAt())
                .updatedAt(facility.getUpdatedAt())
                .build();
    }
}
