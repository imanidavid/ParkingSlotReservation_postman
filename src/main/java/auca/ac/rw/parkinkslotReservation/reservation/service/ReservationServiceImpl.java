package auca.ac.rw.parkinkslotReservation.reservation.service;

import auca.ac.rw.parkinkslotReservation.exception.BusinessRuleException;
import auca.ac.rw.parkinkslotReservation.exception.ResourceNotFoundException;
import auca.ac.rw.parkinkslotReservation.facility.repository.FacilityRepository;
import auca.ac.rw.parkinkslotReservation.reservation.domain.Reservation;
import auca.ac.rw.parkinkslotReservation.reservation.domain.ReservationStatus;
import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationRequest;
import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationResponse;
import auca.ac.rw.parkinkslotReservation.reservation.repository.ReservationRepository;
import auca.ac.rw.parkinkslotReservation.slot.domain.ParkingSlot;
import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final FacilityRepository facilityRepository;

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        validateTimeRange(request);

        ParkingSlot slot = parkingSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with id: " + request.getSlotId()));

        if (reservationRepository.existsOverlapping(slot.getId(), request.getStartTime(), request.getEndTime(), null)) {
            throw new BusinessRuleException(
                    "Slot '" + slot.getSlotNumber() + "' is already booked for the requested time range");
        }

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setVehiclePlate(request.getVehiclePlate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setTotalAmount(computeAmount(slot.getPrice(), request.getStartTime(), request.getEndTime()));

        Reservation saved = reservationRepository.saveAndFlush(reservation);

        slot.setStatus(SlotStatus.RESERVED);
        parkingSlotRepository.save(slot);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ReservationResponse update(UUID id, ReservationRequest request) {
        Reservation reservation = getOrThrow(id);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessRuleException("Only a CONFIRMED reservation can be rescheduled");
        }
        validateTimeRange(request);

        ParkingSlot newSlot = parkingSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with id: " + request.getSlotId()));

        if (reservationRepository.existsOverlapping(newSlot.getId(), request.getStartTime(), request.getEndTime(), id)) {
            throw new BusinessRuleException(
                    "Slot '" + newSlot.getSlotNumber() + "' is already booked for the requested time range");
        }

        ParkingSlot oldSlot = reservation.getSlot();
        boolean slotChanged = !oldSlot.getId().equals(newSlot.getId());

        reservation.setSlot(newSlot);
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setVehiclePlate(request.getVehiclePlate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setTotalAmount(computeAmount(newSlot.getPrice(), request.getStartTime(), request.getEndTime()));

        Reservation saved = reservationRepository.saveAndFlush(reservation);

        if (slotChanged) {
            oldSlot.setStatus(SlotStatus.AVAILABLE);
            parkingSlotRepository.save(oldSlot);
            newSlot.setStatus(SlotStatus.RESERVED);
            parkingSlotRepository.save(newSlot);
        }

        return toResponse(saved);
    }

    @Override
    public ReservationResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Override
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ReservationResponse> findByFacility(UUID facilityId) {
        if (!facilityRepository.existsById(facilityId)) {
            throw new ResourceNotFoundException("Facility not found with id: " + facilityId);
        }
        return reservationRepository.findBySlotFacilityId(facilityId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ReservationResponse cancel(UUID id) {
        Reservation reservation = getOrThrow(id);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessRuleException("Reservation is already " + reservation.getStatus().name().toLowerCase());
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation saved = reservationRepository.saveAndFlush(reservation);

        ParkingSlot slot = reservation.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        parkingSlotRepository.save(slot);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Reservation reservation = getOrThrow(id);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            ParkingSlot slot = reservation.getSlot();
            slot.setStatus(SlotStatus.AVAILABLE);
            parkingSlotRepository.save(slot);
        }
        reservationRepository.delete(reservation);
    }

    private void validateTimeRange(ReservationRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private double computeAmount(double hourlyPrice, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        double hours = Duration.between(start, end).toMinutes() / 60.0;
        return Math.round(hourlyPrice * hours * 100.0) / 100.0;
    }

    private Reservation getOrThrow(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ParkingSlot slot = reservation.getSlot();
        return ReservationResponse.builder()
                .id(reservation.getId())
                .slotId(slot.getId())
                .slotNumber(slot.getSlotNumber())
                .facilityId(slot.getFacility().getId())
                .facilityName(slot.getFacility().getName())
                .customerName(reservation.getCustomerName())
                .customerEmail(reservation.getCustomerEmail())
                .vehiclePlate(reservation.getVehiclePlate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .totalAmount(reservation.getTotalAmount())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}
