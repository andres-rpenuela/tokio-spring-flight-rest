package org.tokio.spring.flight.api.core.exception;

public class OverBookingException extends RuntimeException {

    public OverBookingException(Throwable cause) {
        super(cause);
    }
    public OverBookingException(String message) {
        super(message);
    }
    public OverBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
