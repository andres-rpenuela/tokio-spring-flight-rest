package org.tokio.spring.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
public class BookRequestDTO {
    @NotBlank String title;
    @NotBlank String genre;
    @Positive @NotNull Integer authorId;
}
