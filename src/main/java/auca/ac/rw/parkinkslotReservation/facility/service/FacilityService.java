package auca.ac.rw.parkinkslotReservation.facility.service;

import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityRequest;
import auca.ac.rw.parkinkslotReservation.facility.dto.FacilityResponse;

import java.util.List;
import java.util.UUID;

public interface FacilityService {

    FacilityResponse create(FacilityRequest request);

    FacilityResponse update(UUID id, FacilityRequest request);

    FacilityResponse findById(UUID id);

    List<FacilityResponse> findAll();

    void delete(UUID id);
}
