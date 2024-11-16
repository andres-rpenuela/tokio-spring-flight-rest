package org.tokio.spring.flight.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder
public class PageDTO<T> {

    List<T> items;
    int pageSize;
    int page;
    int total;
}
