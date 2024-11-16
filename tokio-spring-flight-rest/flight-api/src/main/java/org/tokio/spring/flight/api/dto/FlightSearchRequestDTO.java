package org.tokio.spring.flight.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class FlightSearchRequestDTO {
    String number;
    int page;
    int pageSize;
}
