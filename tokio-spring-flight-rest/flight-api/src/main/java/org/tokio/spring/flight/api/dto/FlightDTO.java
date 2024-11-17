package org.tokio.spring.flight.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long id;
    private int version;
    private int capacity;
    private String number;
    private int occupancy;
    private String flightImg;
    private FlightStatusDTO status;
    private LocalDateTime departureTime;
    private String airportDepartureAcronym;
    private String airportArrivalAcronym;
}
