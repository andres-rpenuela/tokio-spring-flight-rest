package org.tokio.spring.common.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PageDTO<T> {

    List<T> items;
    int pageSize;
    int page;
    int total;
}
