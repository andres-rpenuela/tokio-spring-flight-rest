package org.tokio.spring.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.tokio.spring.flight","org.tokio.spring.email","org.tokio.spring.resources"})
public class FlightMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightMvcApplication.class, args);
    }
}
