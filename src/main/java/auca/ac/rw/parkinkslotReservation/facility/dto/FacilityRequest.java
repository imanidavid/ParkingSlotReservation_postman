package auca.ac.rw.parkinkslotReservation.facility.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityRequest {

    @NotBlank(message = "Facility name is required")
    private String name;

    private String address;

    private String city;

    @NotNull(message = "Total slots is required")
    @Positive(message = "Total slots must be greater than zero")
    private Integer totalSlots;
}
