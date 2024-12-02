package org.tokio.spring.flight.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tokio.spring.flight.dto.FlightBookingDTO;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.dto.session.FlightBookingSessionDTO;
import org.tokio.spring.flight.exception.OverBookingException;
import org.tokio.spring.flight.service.FlightBookingSessionService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes(names={"flightBookingSessionDTO"})//lista con los valores de los bean que va ha gestionar el controlador
public class FlightBookingMvcController {

    private final FlightBookingSessionService flightBookingSessionService;
    
    // Esto crea un objeto que esta disponible en el modelo para cualquier handler del controlador
    // ademas, relaciona el atributo de sesion con este atributo del modelo, si no exite
    @ModelAttribute(name = "flightBookingSessionDTO")
    public FlightBookingSessionDTO getFlightBookingSessionDTO() {
        return new FlightBookingSessionDTO();
    }

    @GetMapping("/flight/bookings/new/{flightId}")
    public RedirectView addBookingHandler(@PathVariable(name = "flightId") Long flightId,
                                          @ModelAttribute(value="flightBookingSessionDTO") FlightBookingSessionDTO flightBookingSessionDTO) {
        flightBookingSessionService.addFlightId(flightId,flightBookingSessionDTO);

        return new RedirectView("/flight/bookings");
    }

    // cuando se requiere modificar, entonces post
    // y este debe hacer un redirect
    @PostMapping("/flight/bookings-confirm/")
    public RedirectView confirmBookingHandler(SessionStatus sessionStatus,
                                              @ModelAttribute(value="flightBookingSessionDTO")
                                              FlightBookingSessionDTO flightBookingSessionDTO){
        final FlightBookingDTO flightBookingDTO = flightBookingSessionService
                .confirmFlightBookingSession(flightBookingSessionDTO);

        // limpia los datos de la sesion
        if(flightBookingDTO!=null){
            sessionStatus.setComplete();
            return new RedirectView("/flight/bookings-confirm/%s".formatted(flightBookingDTO.getLocator()));
        }
        return new RedirectView("/flight/bookings");
    }

    @GetMapping("/flight/bookings-confirm/{bookingLocator}")
    public ModelAndView confirmBookingHandler(@PathVariable(name = "bookingLocator") String locator) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("locator", locator);
        modelAndView.setViewName("/flight/bookings/flights-confirm");

        return modelAndView;
    }

    @GetMapping("/flight/bookings")
    public ModelAndView getBookingsHandler(@ModelAttribute(value="flightBookingSessionDTO") FlightBookingSessionDTO flightBookingSessionDTO){

        final Map<Long, FlightDTO> flightMap = flightBookingSessionService.getFlightsById(flightBookingSessionDTO);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("flightBookingSessionDTO", flightBookingSessionDTO);
        modelAndView.addObject("flightMap",flightMap);
        modelAndView.setViewName("/flight/bookings/flights-booking");

        return modelAndView;
    }

    @ExceptionHandler(value = OverBookingException.class)
    public ModelAndView overBookingExceptionHandler(HttpServletRequest request, OverBookingException overBookingException){
        final ModelAndView modelAndView = new ModelAndView("flight/exception");
        modelAndView.addObject("message", overBookingException.getMessage());
        modelAndView.addObject("url",request.getRequestURI());
        return modelAndView;
    }

}
