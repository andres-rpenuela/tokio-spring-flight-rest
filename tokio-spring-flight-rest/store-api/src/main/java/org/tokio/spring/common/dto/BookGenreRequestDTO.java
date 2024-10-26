package org.tokio.spring.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
public class BookGenreRequestDTO {
    @NotBlank String genre;
}
