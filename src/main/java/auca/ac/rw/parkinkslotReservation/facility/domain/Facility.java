package auca.ac.rw.parkinkslotReservation.facility.domain;

import auca.ac.rw.parkinkslotReservation.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Facility extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 150)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;
}
