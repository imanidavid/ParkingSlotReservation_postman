package auca.ac.rw.parkinkslotReservation.reservation.repository;

import auca.ac.rw.parkinkslotReservation.reservation.domain.Reservation;
import auca.ac.rw.parkinkslotReservation.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    boolean existsBySlotIdAndStatus(UUID slotId, ReservationStatus status);

    List<Reservation> findBySlotFacilityId(UUID facilityId);

    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Interval-overlap check: a CONFIRMED reservation on the given slot conflicts with the
     * requested [startTime, endTime) window if it starts before the requested end and ends
     * after the requested start. Excludes {@code excludeId} so reschedules don't collide with themselves.
     */
    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM Reservation r
            WHERE r.slot.id = :slotId
              AND r.status = 'CONFIRMED'
              AND (:excludeId IS NULL OR r.id <> :excludeId)
              AND r.startTime < :endTime
              AND r.endTime > :startTime
            """)
    boolean existsOverlapping(@Param("slotId") UUID slotId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime,
                               @Param("excludeId") UUID excludeId);
}
