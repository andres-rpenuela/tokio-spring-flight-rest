package org.tokio.spring.flight.api.controller.ut;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tokio.spring.flight.api.controller.FlightApiController;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.List;

@WebMvcTest(controllers = FlightApiController.class)
@ActiveProfiles("test")
class FlightApiControllerTest {

    @Autowired
    public MockMvc mvc;

    @MockBean
    private FlightService flightService;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].number").value("0001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].arrival").value("BCN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].departure").value("GLA"))
                .andReturn();
    }

    /** mock **/
    private List<FlightShowDTO> buildShowFlights() {

        return List.of(FlightShowDTO.builder()
                .id(1l)
                .number("0001")
                .arrival("BCN")
                .departure("GLA").build());
    }
}