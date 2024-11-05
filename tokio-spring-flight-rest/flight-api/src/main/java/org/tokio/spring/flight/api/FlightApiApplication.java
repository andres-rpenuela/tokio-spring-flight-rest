package org.tokio.spring.flight.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.tokio.spring.resources","org.tokio.spring.flight.api"})
public class FlightApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightApiApplication.class, args);
    }
}
