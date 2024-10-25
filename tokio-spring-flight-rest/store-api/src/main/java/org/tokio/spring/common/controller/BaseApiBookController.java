package org.tokio.spring.common.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.common.dto.BookDTO;
import org.tokio.spring.common.dto.BookSearchRequestDTO;
import org.tokio.spring.common.dto.PageDTO;
import org.tokio.spring.common.service.BookingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/base/api")
@Validated // aplicar en todos las relgas de validacion
public class BaseApiBookController {

    private final BookingService bookingService;

    @GetMapping("/books")
    public ResponseEntity<PageDTO<BookDTO>> searchBooksHandler(
            @Valid @RequestParam(value="genre",required = false) String genre,
            @Valid @RequestParam(value="page",required = false,defaultValue = "0") int page,
            @Valid @Min(1) @Max(100) @RequestParam(value="page_size",required = false,defaultValue = "10") int pageSize
    ){
        final BookSearchRequestDTO bookSearchRequestDTO = BookSearchRequestDTO.builder()
                .genre(genre)
                .page(page)
                .pageSize(pageSize).build();

        PageDTO<BookDTO> bookDTOPageDTO = bookingService.searchBookByPageIdAndPageSize(bookSearchRequestDTO);

        return ResponseEntity.ok(bookDTOPageDTO);
    }

}
