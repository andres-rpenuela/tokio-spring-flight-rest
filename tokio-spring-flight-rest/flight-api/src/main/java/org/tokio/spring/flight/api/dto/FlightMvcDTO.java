package org.tokio.spring.flight.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class FlightMvcDTO {

    private Long id;
    private int capacity;
    private String flightNumber;
    private String status;
    private LocalDateTime departureTime;
    private String airportDepartureAcronym;
    private String airportArrivalAcronym;
}
