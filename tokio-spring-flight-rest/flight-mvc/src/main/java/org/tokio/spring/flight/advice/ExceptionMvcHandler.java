package org.tokio.spring.flight.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.tokio.spring.flight.dto.ErrorDTO;

@ControllerAdvice(annotations = Controller.class) // Punto para controlar las excepciones, solo
public class ExceptionMvcHandler {

    @ExceptionHandler(value=Exception.class)
    public ModelAndView getExceptionHandler(Exception e, HttpServletRequest request){
        final ErrorDTO error = ErrorDTO.builder()
                .url(request.getRequestURL().toString())
                .exception(e.getMessage()).build();

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", error);
        modelAndView.setViewName("flight/error");

        return modelAndView;
    }
}
