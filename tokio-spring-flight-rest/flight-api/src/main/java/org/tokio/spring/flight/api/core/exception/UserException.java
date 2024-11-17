package org.tokio.spring.flight.api.core.exception;

public class UserException extends RuntimeException {
    public UserException(Throwable cause) {
        super(cause);
    }
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }


}
