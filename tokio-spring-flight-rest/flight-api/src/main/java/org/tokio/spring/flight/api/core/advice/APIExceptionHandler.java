package org.tokio.spring.flight.api.core.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tokio.spring.flight.api.core.exception.FlightException;

//@RestControllerAdvice(annotations = RestController.class) // aplica a todas las excepcions de RestController
@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(FlightException.class)
    public ResponseEntity<String> handleFlightException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.badRequest().body("Request %s bad, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleGenericException(HttpServletRequest request, final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request %s bad, because: %s".formatted(request.getRequestURI(),exception.getMessage()));
    }
}
