package org.tokio.spring.flight.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tokio.spring.flight.api.dto.FlightBookingDTO;
import org.tokio.spring.flight.api.service.FlightBookingService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class FlightBookingApiController {

    private final FlightBookingService flightBookingService;

    @PutMapping("/confirm")
    public ResponseEntity<FlightBookingDTO> confirmBookingHandler(@RequestParam(name = "flight_id") Long flightId,
                                                                  @RequestParam(name = "user_id") String userId) {
        final FlightBookingDTO flightBookingDTO = flightBookingService.newBookingFlight(flightId, userId);
        return ResponseEntity.ok(flightBookingDTO);
    }

    @GetMapping({"","/","/all"})
    public ResponseEntity<Set<FlightBookingDTO>> getAllBookingsHandler() {
        return ResponseEntity.ok( flightBookingService.findAllBooking() );
    }
    @GetMapping("/flights")
    public ResponseEntity<List<FlightBookingDTO>> searchFlightsBookingsByFlightIdHandler(@RequestParam(name = "flight_id") Long flightId) {
        List<FlightBookingDTO> flightBookingDTOS = flightBookingService.searchBookingsByFlightId(flightId);

        return ResponseEntity.ok(flightBookingDTOS);
    }

    @GetMapping("/users")
    public ResponseEntity<List<FlightBookingDTO>> searchFlightsBookingsByUserIdHandler(@RequestParam(name = "user_id") String userId) {
        List<FlightBookingDTO> flightBookingDTOS = flightBookingService.searchBookingsByUserId(userId);

        return ResponseEntity.ok(flightBookingDTOS);
    }
}