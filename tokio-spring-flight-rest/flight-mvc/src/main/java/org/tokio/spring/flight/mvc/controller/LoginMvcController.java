package org.tokio.spring.flight.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginMvcController {

    @GetMapping("/login")
    public String loginHandler() {
        return "flight/login";
    }
}
