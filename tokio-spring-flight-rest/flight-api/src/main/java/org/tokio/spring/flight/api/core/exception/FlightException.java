package org.tokio.spring.flight.api.core.exception;

public class FlightException extends RuntimeException {
    public FlightException(Throwable cause) {
        super(cause);
    }
    public FlightException(String message) {
        super(message);
    }

    public FlightException(String message, Throwable cause) {
        super(message, cause);
    }


}
