package org.tokio.spring.flight.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import org.tokio.spring.flight.api.core.validation.annotation.status.FlightStatusRequired;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Jacksonized
public class FlightMvcDTO {

    private Long id;

    @PositiveOrZero
    @Digits(fraction = 0, integer = 3) // no se fracciona, y 3 digitos
    private int capacity;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}\\d{4}")
    private String flightNumber;

    @FlightStatusRequired(target = FlightStatusDTO.class)
    private String status;

    @NotNull // para objeto
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") // conversión de datos como formularios o cuando los datos se envían como parámetros de URL
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm") // se aplica automáticamente en los formatos de JSON
    private LocalDateTime departureTime;

    @NotBlank
    private String airportDepartureAcronym;

    @NotBlank
    private String airportArrivalAcronym;

    private String resourceId; // TODO mapper
}
