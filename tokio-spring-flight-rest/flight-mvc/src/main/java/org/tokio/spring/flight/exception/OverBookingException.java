package org.tokio.spring.flight.exception;

public class OverBookingException extends RuntimeException {

    public OverBookingException(String message) {
        super(message);
    }

    public OverBookingException(Throwable cause) {
        super(cause);
    }

    public OverBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
