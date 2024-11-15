package org.tokio.spring.flight.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.validation.binding.flight.FlightMvcDTOCustomValidator;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;
import org.tokio.spring.flight.api.dto.FlightMvcResponseDTO;
import org.tokio.spring.flight.api.dto.FlightShowDTO;
import org.tokio.spring.flight.api.service.FlightService;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    public ResponseEntity<FlightMvcResponseDTO> createFlight(
            @Valid @RequestPart(name = "flightMvcDTO") FlightMvcDTO flightMvcDTO, BindingResult bindingResult,
            @RequestParam(name = "image", required = false) MultipartFile multipartFile,
            @RequestParam(name = "description", required = false) String description) throws FlightException{
        Map<String, String> errores = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED;
        // Verificamos si hay errores de validación
        if(bindingResult.hasErrors()){

            // Verificamos si hay errores de validación
            errores = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()) )
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            status = HttpStatus.BAD_REQUEST;
        }else{

            // Si no hay errores, continuamos con el procesamiento del usuario
            if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
                flightMvcDTO = flightService.createFlight(flightMvcDTO);
            } else {
                flightMvcDTO = flightService.createFlight(flightMvcDTO, multipartFile, description);
            }

        }

        final FlightMvcResponseDTO response = new FlightMvcResponseDTO(errores,flightMvcDTO);
        return ResponseEntity.status(status).body(response);
    }


    @PutMapping("/updated")
    public ResponseEntity<FlightMvcResponseDTO> updateFlight(
            @Valid @RequestPart(name="flightMvcDTO") FlightMvcDTO flightMvcDTO, BindingResult bindingResult,
            @RequestParam(name = "image",required = false) MultipartFile multipartFile,
            @RequestParam(name = "description",required = false) String description) throws FlightException{
        // TODO actualizar vuelo + validacion de datos + test
        if(flightMvcDTO.getId() == null){
            throw new FlightException("The id of flights is null");
        }
        Map<String, String> errors = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if(bindingResult.hasErrors()){
            status = HttpStatus.BAD_REQUEST;
            errors =  bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> Objects.nonNull(fieldError.getDefaultMessage()) )
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        }else{
            // Si no hay errores, continuamos con el procesamiento del usuario
            if(multipartFile.isEmpty()){
                flightMvcDTO = flightService.updated(flightMvcDTO);
            }else{
                flightMvcDTO = flightService.updated(flightMvcDTO,multipartFile,description);
            }

        }

        final FlightMvcResponseDTO response = new FlightMvcResponseDTO(errors,flightMvcDTO);
        return ResponseEntity.status(status).body(response);
    }


    @RequestMapping(value = "/removeImg",method = {GET,DELETE})
    public ResponseEntity<FlightMvcDTO> removeResourceHandler(@RequestParam(value = "idFlight") Long idFlight,
                                              @RequestParam(value = "rsc") UUID resourceId){
        return ResponseEntity.ok()
                .body(flightService.deleteImageAndGet(idFlight,resourceId));
    }
}
