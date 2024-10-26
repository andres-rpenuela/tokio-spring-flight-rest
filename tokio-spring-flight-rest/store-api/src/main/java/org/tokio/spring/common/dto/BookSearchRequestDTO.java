package org.tokio.spring.common.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
public class BookSearchRequestDTO {
    String genre;
    int page;
    int pageSize;
}
