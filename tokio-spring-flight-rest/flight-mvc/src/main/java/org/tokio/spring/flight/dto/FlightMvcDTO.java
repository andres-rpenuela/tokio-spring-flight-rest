package org.tokio.spring.flight.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.tokio.spring.flight.validation.annotation.EnumValid;

import java.time.LocalDateTime;

/**
 * Modelo de datos que va enviar la interfaz grafica al controlador
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightMvcDTO {
    private Long id;
    @NotNull    @PositiveOrZero
    @Digits(fraction = 0, integer = 3) // no se fracciona, y 3 digitos
    private int capacity;
    @NotBlank
    private String number;
    //@NotEmpty @NotBlank
    @EnumValid(required = true,target = STATUS_FLIGHT_DTO.class,message = "{validation.flight.status.invalid}")
    private String status;
    //@NotBlank
    //private String dayTime;
    @NotNull // para objeto
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    LocalDateTime dayTime;
    @NotBlank
    private String airportDepartureAcronym;
    @NotBlank
    private String airportArrivalAcronym;
}
