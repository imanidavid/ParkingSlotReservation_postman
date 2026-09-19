package auca.ac.rw.parkinkslotReservation.facility.repository;

import auca.ac.rw.parkinkslotReservation.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}
