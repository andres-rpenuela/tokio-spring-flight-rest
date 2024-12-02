package org.tokio.spring.flight.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/flight")
public class IndexMvcController {

    @GetMapping
    public String getIndexPageHandler() {
        return "/flight/index";
    }

    @GetMapping("/my-error")
    public String getErrorHandler() {
        throw new RuntimeException("My error handler: %s".formatted(LocalDateTime.now()));
    }
}
