package auca.ac.rw.parkinkslotReservation.config;

import auca.ac.rw.parkinkslotReservation.facility.domain.Facility;
import auca.ac.rw.parkinkslotReservation.facility.repository.FacilityRepository;
import auca.ac.rw.parkinkslotReservation.slot.domain.ParkingSlot;
import auca.ac.rw.parkinkslotReservation.slot.domain.SlotStatus;
import auca.ac.rw.parkinkslotReservation.slot.domain.VehicleType;
import auca.ac.rw.parkinkslotReservation.slot.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FacilityRepository facilityRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    @Override
    public void run(String... args) {
        if (facilityRepository.count() > 0) {
            return;
        }

        Facility mainCampus = new Facility();
        mainCampus.setName("Main Campus Parking");
        mainCampus.setAddress("KG 7 Ave");
        mainCampus.setCity("Kigali");
        mainCampus.setTotalSlots(5);
        facilityRepository.save(mainCampus);

        Facility basementGarage = new Facility();
        basementGarage.setName("Basement Garage");
        basementGarage.setAddress("KN 4 St");
        basementGarage.setCity("Kigali");
        basementGarage.setTotalSlots(3);
        facilityRepository.save(basementGarage);

        parkingSlotRepository.save(newSlot("SLOT-A1", VehicleType.CAR, 500.0, mainCampus));
        parkingSlotRepository.save(newSlot("SLOT-A2", VehicleType.CAR, 500.0, mainCampus));
        parkingSlotRepository.save(newSlot("SLOT-A3", VehicleType.MOTORCYCLE, 200.0, mainCampus));

        parkingSlotRepository.save(newSlot("SLOT-B1", VehicleType.CAR, 600.0, basementGarage));
        parkingSlotRepository.save(newSlot("SLOT-B2", VehicleType.TRUCK, 1500.0, basementGarage));
    }

    private ParkingSlot newSlot(String slotNumber, VehicleType vehicleType, double price, Facility facility) {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber(slotNumber);
        slot.setVehicleType(vehicleType);
        slot.setPrice(price);
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setFacility(facility);
        return slot;
    }
}
