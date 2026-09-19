package auca.ac.rw.parkinkslotReservation.reservation.service;

import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationRequest;
import auca.ac.rw.parkinkslotReservation.reservation.dto.ReservationResponse;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponse create(ReservationRequest request);

    ReservationResponse update(UUID id, ReservationRequest request);

    ReservationResponse findById(UUID id);

    List<ReservationResponse> findAll();

    List<ReservationResponse> findByFacility(UUID facilityId);

    ReservationResponse cancel(UUID id);

    void delete(UUID id);
}
