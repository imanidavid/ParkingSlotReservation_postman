package auca.ac.rw.parkinkslotReservation.exception;

/**
 * Raised when a request is well-formed but violates a domain business rule
 * (e.g. slot overlap, capacity exceeded, deleting a facility that still has slots).
 * Mapped to HTTP 409 Conflict by {@link GlobalExceptionHandler}.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
