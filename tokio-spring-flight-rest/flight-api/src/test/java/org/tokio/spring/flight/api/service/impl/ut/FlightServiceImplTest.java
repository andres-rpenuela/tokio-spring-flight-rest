package org.tokio.spring.flight.api.service.impl.ut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.flight.api.domain.Airport;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.service.impl.FlightServiceImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FlightServiceImplTest {

    @InjectMocks
    private FlightServiceImpl service;

    @Mock
    private FlightReport flightReport;

    @Mock
    private ModelMapper modelMapper;
    @Test
    void givenCallMethod_whenGetAllFlights_thenReturnFlights() {

        Mockito.when(flightReport.findAll()).thenReturn( buildFlightDTOListMock() );
        List<FlightShowDTO> flightShowDTOSs = service.getAllFlights();

        Assertions.assertThat(flightShowDTOSs).isNotNull()
                .hasSize(1)
                .first()
                .returns(buildFlightDTOListMock().getFirst().getAirportArrival().getAcronym(),FlightShowDTO::arrival)
                .returns(buildFlightDTOListMock().getFirst().getAirportDeparture().getAcronym(),FlightShowDTO::departure)
                .returns(buildFlightDTOListMock().getFirst().getNumber(),FlightShowDTO::number)
                .returns(buildFlightDTOListMock().getFirst().getId(),FlightShowDTO::id);
    }


    private List<Flight> buildFlightDTOListMock() {
        final Airport airportArrival = Airport.builder()
                .acronym("BCA")
                .name("Barcelona").build();

        final Airport departureAirport = Airport.builder()
                .acronym("GLA")
                .name("GALICIA").build();

        return List.of(Flight.builder()
                .id(1l)
                .number("0001")
                .statusFlight(STATUS_FLIGHT.SCHEDULED)
                .airportArrival(airportArrival)
                .airportDeparture(departureAirport).build());
    }

}