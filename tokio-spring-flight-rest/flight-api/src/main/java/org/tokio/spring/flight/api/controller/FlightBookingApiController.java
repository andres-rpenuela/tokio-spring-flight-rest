package org.tokio.spring.flight.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.flight.api.dto.FlightBookingDTO;
import org.tokio.spring.flight.api.service.FlightBookingService;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class FlightBookingApiController {

    private final FlightBookingService flightBookingService;

    @PutMapping("/confirm")
    public ResponseEntity<FlightBookingDTO> confirmBookingHandler(@RequestParam(name = "flight_id") Long flightId,
                                                                  @RequestParam(name = "user_id") String userId) {
        final  FlightBookingDTO flightBookingDTO = flightBookingService.newBookingFlight(flightId,userId);
        return ResponseEntity.ok(flightBookingDTO);
    }

}
