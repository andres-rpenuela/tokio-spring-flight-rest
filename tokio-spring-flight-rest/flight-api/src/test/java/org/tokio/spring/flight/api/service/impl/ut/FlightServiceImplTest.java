package org.tokio.spring.flight.api.service.impl.ut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.flight.api.domain.Airport;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.service.impl.FlightServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    void givenCallMethod_whenGetAllShowFlights_thenReturnShowFlights() {

        Mockito.when(flightReport.findAll()).thenReturn( buildFlightListMock() );
        List<FlightShowDTO> flightShowDTOSs = service.getAllShowFlights();

        Assertions.assertThat(flightShowDTOSs).isNotNull()
                .hasSize(1)
                .first()
                .returns(buildFlightListMock().getFirst().getAirportArrival().getAcronym(),FlightShowDTO::arrival)
                .returns(buildFlightListMock().getFirst().getAirportDeparture().getAcronym(),FlightShowDTO::departure)
                .returns(buildFlightListMock().getFirst().getNumber(),FlightShowDTO::number)
                .returns(buildFlightListMock().getFirst().getId(),FlightShowDTO::id);
    }

    @Test
    void givenCallMethod_whenGetAllMvcFlights_thenReturnShowFlights() {

        // Arrange - Preparar el mock de `flightReport` y `modelMapper`
        final List<Flight> flightListMock = buildFlightListMock();
        Mockito.when(flightReport.findAll()).thenReturn(flightListMock);

        // se realiza el mepeo para cada objeto de entrada
        final ModelMapper mapper = new ModelMapper();
        Mockito.when(modelMapper.map(any(Flight.class), eq(FlightMvcDTO.class))).thenAnswer(invocationOnMock ->
            mapper.map(invocationOnMock.getArgument(0), FlightMvcDTO.class)
        );

        // Act - Llamar al metodo bajo prueba
        List<FlightMvcDTO> flightMvcDTOS = service.getAllMvcFlights();

        // Assert - Validar los resultados
        Assertions.assertThat(flightMvcDTOS).isNotNull()
                .hasSize(1)
                .first()
                .returns(buildFlightListMock().getFirst().getAirportArrival().getAcronym(),FlightMvcDTO::getAirportArrivalAcronym)
                .returns(buildFlightListMock().getFirst().getAirportDeparture().getAcronym(),FlightMvcDTO::getAirportDepartureAcronym)
                .returns(buildFlightListMock().getFirst().getNumber(),FlightMvcDTO::getFlightNumber)
                .returns(buildFlightListMock().getFirst().getId(),FlightMvcDTO::getId);
    }

    @Test
    void givenFlightId_whenGetFindFLightById_thenReturnMvcFlightDTO() {

        // Arrange - Preparar el mock de `flightReport` y `modelMapper`
        final Flight flight = buildFlightListMock().getFirst();
        Mockito.when(flightReport.findById(flight.getId())).thenReturn( Optional.of(flight) );

        // se realiza el mepeo para cada objeto de entrada
        final ModelMapper mapper = new ModelMapper();
        Mockito.when(modelMapper.map(any(Flight.class), eq(FlightMvcDTO.class))).thenAnswer(invocationOnMock ->
                mapper.map(invocationOnMock.getArgument(0), FlightMvcDTO.class)
        );

        // Act - Llamar al metodo bajo prueba
        FlightMvcDTO flightMvcDTO = service.getFlightById(flight.getId());

        // Assert - Validar los resultados
        Assertions.assertThat(flightMvcDTO).isNotNull()
                .returns(flight.getAirportArrival().getAcronym(),FlightMvcDTO::getAirportArrivalAcronym)
                .returns(flight.getAirportDeparture().getAcronym(),FlightMvcDTO::getAirportDepartureAcronym)
                .returns(flight.getNumber(),FlightMvcDTO::getFlightNumber)
                .returns(flight.getId(),FlightMvcDTO::getId);
    }



    private List<Flight> buildFlightListMock() {
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