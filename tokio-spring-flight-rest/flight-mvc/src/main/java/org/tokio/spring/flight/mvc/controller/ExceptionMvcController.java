package org.tokio.spring.flight.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ExceptionMvcController {

    @ExceptionHandler
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception exception){
        log.error("Error: %s".formatted(exception.getMessage()), exception);

        final ModelAndView modelAndView = new ModelAndView("flight/exception");
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.addObject("url",request.getRequestURI());
        return modelAndView;
    }
}
