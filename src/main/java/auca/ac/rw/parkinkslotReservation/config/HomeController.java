package auca.ac.rw.parkinkslotReservation.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", "Parking Slot Reservation API");
        body.put("status", "UP");
        body.put("endpoints", Map.of(
                "facilities", "/api/facilities",
                "slots", "/api/slots",
                "reservations", "/api/reservations"
        ));
        return body;
    }
}
