package org.tokio.spring.flight.mvc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tokio.spring.flight.dto.FlightMvcDTO;
import org.tokio.spring.flight.service.AirportService;
import org.tokio.spring.flight.service.FlightService;
import org.tokio.spring.flight.validation.binding.FlightMvcDTOValidator;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/flight")
public class FlightMvcController {

    private final FlightService flightService;
    private final AirportService airportService;
    private final ModelMapper modelMapper;

    /** binding validators **/
    private final FlightMvcDTOValidator flightMvcDTOValidator;

    @InitBinder
    public final void flightMvcDTOValidator(WebDataBinder webDataBinder){
        webDataBinder.setValidator(flightMvcDTOValidator);
    }

    /** mappers **/
    @GetMapping(value={"/flights","/flights/","/flights/list"})
    public ModelAndView listFlightsHandler(){

        final ModelAndView modelAndView =  new ModelAndView();
        modelAndView.setViewName("flight/flights-list");
        modelAndView.addObject("flights", flightService.findAllFlights());
        return modelAndView;
    }

    @GetMapping(value={"/flight-edit","/flight-edit/","/flight-edit/{flightId}"})
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ModelAndView  flightCreateOrEditHandler(@PathVariable(name="flightId",required = false) Long flightId,
                                                   Model model){
        final FlightMvcDTO flightMvcDTO = Optional.ofNullable(flightId)
                .map(flightService::findById)
                .map(dto -> modelMapper.map(dto, FlightMvcDTO.class))
                .orElse(FlightMvcDTO.builder().build());

        final ModelAndView modelAndView =  new ModelAndView();
        modelAndView.setViewName("flight/flights-edit");
        modelAndView.addAllObjects(model.asMap());
        if(!model.containsAttribute("flight")){
            // si no existe en el modelo
            modelAndView.addObject("flight", flightMvcDTO);
        }
        //modelAndView.addObject("flight", flightMvcDTO);
        modelAndView.addObject("airports", airportService.getAllAirports());
        modelAndView.addObject("resourceImageId",
                Optional.ofNullable(flightMvcDTO.getId())
                        .map(flightService::getResourceIdByFlightId)
                        .map(UUID::fromString)
                        .orElse(null));

        return modelAndView;
    }

    /*
    @PostMapping(value={"/flight-edit","/flight-edit/","/flight-edit/{flightId}"})
    public ModelAndView  flightCreateOrEditHandler(@PathVariable(name="flightId",required = false) Long flightId,
                                                   @RequestParam("image") MultipartFile multipartFile, // campo del from
                                                   @Valid @ModelAttribute("flight") FlightMvcDTO flightMvcDto, BindingResult bindingResult,  // del modelo se coge el objeto que se llame flight
                                                   Model model){

        if(bindingResult.hasErrors()){
            final ModelAndView modelAndView =  new ModelAndView();
            modelAndView.setViewName("flight/flights-edit");
            modelAndView.addObject("flight", flightMvcDto);
            modelAndView.addObject("airports", airportService.getAllAirports());
            modelAndView.addObject("resourceImageId",
                    Optional.ofNullable(flightMvcDto.getId())
                            .map(flightService::getResourceIdByFlightId)
                            .map(UUID::fromString)
                            .orElse(null));
            return modelAndView;
        }

        if(flightId==null){
            flightService.createFlight(flightMvcDto,multipartFile);
        }else{
            flightService.editFlight(flightMvcDto,multipartFile);
        }

        // redirect
        return new ModelAndView("redirect:/flight/flights");
    }*/

    @PostMapping(value={"/flight-edit","/flight-edit/","/flight-edit/{flightId}"})
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public RedirectView  flightCreateOrEditHandler(@PathVariable(name="flightId",required = false) Long flightId,
                                                   @RequestParam("image") MultipartFile multipartFile, // campo del from
                                                   @Valid @ModelAttribute("flight") FlightMvcDTO flightMvcDto, BindingResult bindingResult,  // del modelo se coge el objeto que se llame flight
                                                   RedirectAttributes redirectAttributes,
                                                   Model model){

        if(bindingResult.hasErrors()){
            // se crea un nuevo get a partir de los datos del formulario
            final ModelAndView modelAndView =  new ModelAndView();
            //modelAndView.setViewName("flight/flights-edit");
            modelAndView.addAllObjects(model.asMap());
            if(!model.containsAttribute("flight")){
                modelAndView.addObject("flight", flightMvcDto);
            }
            //modelAndView.addObject("flight", flightMvcDto);
            modelAndView.addObject("airports", airportService.getAllAirports());
            modelAndView.addObject("resourceImageId",
                    Optional.ofNullable(flightMvcDto.getId())
                            .map(flightService::getResourceIdByFlightId)
                            .map(UUID::fromString)
                            .orElse(null));
            // se converte el model and view en elementos y lo adaptos a elemtentos redireccionables
            final String maybeParam = Optional.ofNullable(flightId)
                    .map("/%d"::formatted)
                    .orElse(StringUtils.EMPTY);
            modelAndView.getModel().forEach(redirectAttributes::addFlashAttribute);
            // se envia una peticion nueva
            return new RedirectView("/flight/flight-edit%s".formatted(maybeParam));
        }

        if(flightId==null){
            flightService.createFlight(flightMvcDto,multipartFile);
        }else{
            flightService.editFlight(flightMvcDto,multipartFile);
        }

        // redirect
        return new RedirectView("/flight/flights");
    }

    @GetMapping("/removeImg")
    public RedirectView removeResourceHandler(@RequestParam(value = "idFlight") Long idFlight,
                                              @RequestParam(value = "rsc") UUID resourceId){
        flightService.deleteImage(resourceId);

        return new RedirectView("/flight/flight-edit/"+idFlight);
    }

}
