package org.tokio.spring.flight.api.controller.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.tokio.spring.flight.api.controller.FlightApiController;
import org.tokio.spring.flight.api.core.validation.binding.flight.FlightMvcDTOCustomValidator;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.dto.FlightStatusDTO;
import org.tokio.spring.flight.api.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebMvcTest(controllers = FlightApiController.class)
@ActiveProfiles("test")
class FlightApiControllerTest {

    @Autowired
    public MockMvc mvc;

    @MockBean
    private FlightService flightService;

    /** mock validate binding **/
    @SpyBean
    private FlightMvcDTOCustomValidator flightMvcDTOCustomValidator;

    // para convertir objetos a JSON
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenGetRequest_whenListFlightsHandler_thenReturnListOfFlights() throws Exception {
        final String requestUrl = "/api/flights/show-flights";
        Mockito.when(flightService.getAllShowFlights()).thenReturn(buildShowFlights());
        // Perform the request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(requestUrl))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].number").value("BCN0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].arrival").value("BCN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].departure").value("GLA"))
                .andReturn();
    }

    @Test
    void givenGetRequest_whenRecoverFlightsHandler_thenReturnListOfFlights() throws Exception {
        final String requestUrl = "/api/flights/list";
        Mockito.when(flightService.getAllMvcFlights()).thenReturn(buildFlightMvcDTOS());
        // Perform the request
        MvcResult result = mvc.perform( MockMvcRequestBuilders.get(requestUrl) )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].flightNumber").value("BCN0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].airportArrivalAcronym").value("BCN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].airportDepartureAcronym").value("GLA"))
                .andReturn();
    }

    @Test
    void givenFlightMvcDTOInvalid_whenCreatedNew_thenReturnListOfFlights() throws Exception {
        final String requestUrl = "/api/flights/created";

        // Convertir el objeto DTO a JSON
        final String flightMvcDTO = objectMapper.writeValueAsString( FlightMvcDTO.builder().build() );

        // Perform the request
        MvcResult result = mvc.perform( MockMvcRequestBuilders.post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightMvcDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.size()").value(Matchers.greaterThan(3)))   // Verifica que hay mas de 3 errores
                .andReturn();
    }

    @Test
    void whenValidFlight_thenReturns200() throws Exception {
        // Configurar el validador para no agregar errores en el caso de una solicitud válida
        //Mockito.doNothing().when(flightMvcDTOCustomValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));

        String validFlightJson = """                
            {
                "capacity": 100,
                "flightNumber": "BCN0001",
                "status": "SCHEDULED",
                "departureTime": "28-10-2024 21:13",
                "airportDepartureAcronym": "BCN",
                "airportArrivalAcronym": "GLA"
                }
        """;

        mvc.perform(MockMvcRequestBuilders.post("/api/flights/created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFlightJson))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void whenInvalidFlight_thenReturns400() throws Exception {
        // Configurar el validador para agregar errores en el caso de una solicitud inválida
        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("capacity", "CapacityError", "La capacidad debe ser mayor a 0");
            return null;
        }).when(flightMvcDTOCustomValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));

        String invalidFlightJson = """
            {
                "id": 1,
                "capacity": 0,
                "flightNumber": "000000001T",
                "status": "SCHEDULED",
                "departureTime": "28-10-2024 21:13",
                "airportDepartureAcronym": "BCN",
                "airportArrivalAcronym": "MAD"
            }
        """;

        mvc.perform(MockMvcRequestBuilders.post("/api/flights/created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFlightJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.capacity").value("La capacidad debe ser mayor a 0"));
    }

    @Test
    void whenInvalidFlight_thenReturns500() throws Exception {
        // Configurar el validador para agregar errores en el caso de una solicitud inválida
        Mockito.doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("capacity", "CapacityError", "La capacidad debe ser mayor a 0");
            return null;
        }).when(flightMvcDTOCustomValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));

        String invalidFlightJson = """
            {
                "id": 1,
                "capacity": 0,
                "flightNumber": "000000001T",
                "status": "SCHEDULED",
                "departureTime": "22024-06-07T14:00:00",
                "airportDepartureAcronym": "BCN",
                "airportArrivalAcronym": "MAD"
            }
        """;

        mvc.perform(MockMvcRequestBuilders.post("/api/flights/created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFlightJson))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    void givenFlightMvcDTOWithArrivalAndDepartureSame_whenCreatedNew_thenReturnListOfFlightsWithError() throws Exception {
        final String requestUrl = "/api/flights/created";

        // Convertir el objeto DTO a JSON
        final String flightMvcDTO = objectMapper.writeValueAsString( FlightMvcDTO.builder()
                .id(1l)
                .flightNumber("BCN0001")
                .airportArrivalAcronym("BCN")
                .departureTime(buildDepartureTimeMock())
                .status(FlightStatusDTO.SCHEDULED.toString())
                .airportDepartureAcronym("BCN").build()
        );


        // Perform the request
        MvcResult result = mvc.perform( MockMvcRequestBuilders.post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightMvcDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.size()").value(Matchers.equalTo(1)))   // Verifica que hay mas de 3 errores
                .andReturn();
    }
    @Test
    void givenFlightMvcDtoValid_whenCreatedNew_thenReturnListOfFlightsWithOutErrors() throws Exception {
        final String requestUrl = "/api/flights/created";

        final String flightMvcDTO = objectMapper.writeValueAsString( buildFlightMvcDTO());
        Mockito.when(flightService.createFlight(buildFlightMvcDTO())).thenReturn(buildFlightMvcDTO());

        // Perform the request
        MvcResult result = mvc.perform( MockMvcRequestBuilders.post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightMvcDTO))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.anEmptyMap()))   // Verifica que hay mas de 3 errores
                .andReturn();

        // Obtener el contenido JSON de la respuesta
        String jsonResponse = result.getResponse().getContentAsString();

        // Convertir el JSON en un Map
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

        // Extraer el JSON específico del objeto "flightMvcDTO"
        String flightMvcDTOJson = objectMapper.writeValueAsString(responseMap.get("flightMvcDTO"));

        // Convertir el JSON en un objeto FlightMvcDTO
        FlightMvcDTO responseFlightMvcDTO = objectMapper.readValue(flightMvcDTOJson, FlightMvcDTO.class);

        Assertions.assertThat(responseFlightMvcDTO)
                .isNotNull()
                .returns(buildFlightMvcDTO().getId(),FlightMvcDTO::getId)
                .returns(buildFlightMvcDTO().getFlightNumber(),FlightMvcDTO::getFlightNumber)
                .returns(buildFlightMvcDTO().getAirportArrivalAcronym(),FlightMvcDTO::getAirportArrivalAcronym)
                .returns(buildFlightMvcDTO().getAirportDepartureAcronym(),FlightMvcDTO::getAirportDepartureAcronym)
                .returns(buildFlightMvcDTO().getDepartureTime(),FlightMvcDTO::getDepartureTime);
    }

    /** mock **/
    private List<FlightShowDTO> buildShowFlights() {

        return List.of(FlightShowDTO.builder()
                .id(1l)
                .number("BCN0001")
                .arrival("BCN")
                .departure("GLA").build());
    }

    private FlightMvcDTO buildFlightMvcDTO() {

        return FlightMvcDTO.builder()
                .id(1l)
                .flightNumber("BCN0001")
                .airportArrivalAcronym("BCN")
                .departureTime(buildDepartureTimeMock())
                .status(FlightStatusDTO.SCHEDULED.toString())
                .airportDepartureAcronym("GLA").build();
    }

    private LocalDateTime buildDepartureTimeMock(){
        return LocalDateTime.of(2024,6,7,14,0,0);
    }
    private List<FlightMvcDTO> buildFlightMvcDTOS(){
        return List.of(buildFlightMvcDTO());
    }
}