package org.tokio.spring.flight.dto.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class FlightBookingSessionDTO {

    private Long currentFlightId;
    @Builder.Default
    private Set<Long> discardedFlightIds = new HashSet<>();
}
