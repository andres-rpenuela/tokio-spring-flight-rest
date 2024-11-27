package org.tokio.spring.flight.dto;

public interface AccountFlightDto {
    String getId();
    String getNumber();
    String getAirportDepartureAcronym();
    String getAirportArrivalAcronym();
    int getAccount();
}
