package org.tokio.spring.flight.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.validation.binding.flight.FlightMvcDTOCustomValidator;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightMvcResponseDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightApiController {

    private final FlightService flightService;

    /** binding validators **/
    private final FlightMvcDTOCustomValidator flightMvcDTOCustomValidator;

    @InitBinder
    public final void flightMvcDTOCustomValidator(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() instanceof FlightMvcDTO) {
            webDataBinder.setValidator(flightMvcDTOCustomValidator);
        }
    }

    @GetMapping({"/show-flights"})
    public ResponseEntity<List<FlightShowDTO>> listShowFlightsHandler(){

        List<FlightShowDTO> flightShowDTOS = flightService.getAllShowFlights();
        return ResponseEntity.ok(flightShowDTOS);
    }

    @GetMapping({"/list","","/"})
    public ResponseEntity<List<FlightMvcDTO>> listFlightsHandler(){

        List<FlightMvcDTO> flightMvcDTOS = flightService.getAllMvcFlights();
        return ResponseEntity.ok(flightMvcDTOS);
    }

    @GetMapping("/recover/{id}")
    public ResponseEntity<FlightMvcDTO> recoverFlight(@PathVariable(value = "id") Long id) throws FlightException{
        final FlightMvcDTO flightMvcDTO = flightService.getFlightById(id);
        return ResponseEntity.ok(flightMvcDTO);
    }

    @PostMapping("/created")
        public ResponseEntity<FlightMvcResponseDTO> createFlight(@Valid @RequestBody FlightMvcDTO flightMvcDTO, BindingResult bindingResult) throws FlightException{

        // Verificamos si hay errores de validación
        if(bindingResult.hasErrors()){

            // Verificamos si hay errores de validación
            final Map<String, String> errores = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()) )
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            final FlightMvcResponseDTO response = new FlightMvcResponseDTO(errores,flightMvcDTO);
            return ResponseEntity.badRequest().body(response);
        }

        // Si no hay errores, continuamos con el procesamiento del usuario
        // TODO implemntar logica de sercicio para crear
        final FlightMvcResponseDTO response = new FlightMvcResponseDTO(Collections.emptyMap(),flightMvcDTO);
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/updated/{id}")
    public ResponseEntity<FlightMvcDTO> updateFlight(
            @PathVariable(value = "id") Long id,
            @RequestBody FlightMvcDTO flightMvcDTO) throws FlightException{
        // TODO actualizar vuelo + validacion de datos + test
        return null;
    }

}
