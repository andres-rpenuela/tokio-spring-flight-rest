package org.tokio.spring.common.controller.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tokio.spring.common.controller.BaseApiBookController;
import org.tokio.spring.common.dto.BookDTO;
import org.tokio.spring.common.dto.BookSearchRequestDTO;
import org.tokio.spring.common.dto.PageDTO;
import org.tokio.spring.common.service.BookingService;

import java.util.List;

@WebMvcTest(controllers = BaseApiBookController.class)
@ActiveProfiles("test")
class BaseApiBookControllerTest {

    @Autowired
    public MockMvc mvc;

    @Captor
    ArgumentCaptor<BookSearchRequestDTO> requestCaptor;

    @MockBean
    private BookingService bookingService;

    // Instantiate the ObjectMapper
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenRequestSearchBooksWithOutGenre_whenSearchBooksHandler_returnPageBooks() throws Exception {
        final String requestUrl = "/base/api/books";
        BDDMockito.given(bookingService.searchBookByPageIdAndPageSize(requestCaptor.capture()))
                .willReturn(PageDTO.<BookDTO>builder()
                        .page(0)
                        .pageSize(10)
                        .items(List.of())
                        .build());
        // Perform the request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(requestUrl))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)) //"application/json"
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").exists())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Convert the response JSON to PageDTO<BookDTO>
        PageDTO<BookDTO> pageDTO = objectMapper.readValue(response.getContentAsString(), PageDTO.class);

        Assertions.assertThat(requestCaptor.getValue())
                .isNotNull()
                .returns(pageDTO.getPage(),BookSearchRequestDTO::getPage)
                .returns(pageDTO.getPageSize(),BookSearchRequestDTO::getPageSize);
    }
}