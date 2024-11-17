package org.tokio.spring.flight.api.core.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.exception.OverBookingException;
import org.tokio.spring.flight.api.core.exception.UserException;

//@RestControllerAdvice(annotations = RestController.class) // aplica a todas las excepcions de RestController
@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(FlightException.class)
    public ResponseEntity<String> handleFlightException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.badRequest().body("Request %s bad, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }

    @ExceptionHandler(OverBookingException.class)
    public ResponseEntity<String> handleFlightBookingException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.badRequest().body("Booking of request %s cancelled, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.badRequest().body("Problems with the user in the request %s, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleGenericException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request %s bad, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }
}
